package org.testadirapa.components

import dev.inmo.krontab.doInfinity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.testadirapa.models.cardtrader.Blueprint
import org.testadirapa.models.cardtrader.MtgLanguage
import org.testadirapa.models.cardtrader.Product
import org.testadirapa.models.db.Watcher
import org.testadirapa.services.CardTraderService
import org.testadirapa.services.CouchDbService

class PriceChecker private constructor(
	private val cardTraderService: CardTraderService,
	private val couchDbService: CouchDbService
) {

	companion object {
		private lateinit var instance: PriceChecker

		operator fun invoke(cardTraderService: CardTraderService, couchDbService: CouchDbService): PriceChecker {
			if (!::instance.isInitialized) {
				instance = PriceChecker(cardTraderService, couchDbService)
			}
			return instance
		}
	}

	private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

	fun launchCheckJob() = scope.launch {
		while (isActive) {
			println("Start coroutine")
			doInfinity("0 */10 * * * *") {
				try {
					println("Start job")
					checkProducts()
				} catch (e: Exception) {
					println("Exception while checking prices ${e.message}")
				}
			}
		}
	}

	private suspend fun checkProducts() {
		val blueprintIds = couchDbService.getBlueprintIdsForAllWatchers()
		blueprintIds.forEach {
			try {
				checkProduct(it)
			} catch (e: Exception) {
				println("Exception while checking blueprint $it ${e.message}")
			}
		}
	}

	private suspend fun checkProduct(blueprintId: Long) {
		val blueprint = couchDbService.getBlueprint(blueprintId) ?: error("Blueprint $blueprintId does not exist")
		val watchers = couchDbService.getWatchersByBlueprintId(blueprintId).toMutableSet()
		val activatedWatchers = mutableListOf<Pair<Product, Watcher>>()

		cardTraderService.searchProducts(blueprint.id).forEach { product ->
			val iterator = watchers.iterator()
			while (iterator.hasNext()) {
				val nextWatcher = iterator.next()
				if (nextWatcher.matches(product)) {
					activatedWatchers.add(product to nextWatcher)
					iterator.remove()
				}
			}
		}

		activatedWatchers.filter {
			!it.second.triggered
		}.forEach { (product, watcher) ->
			AsyncMessageQueue.sendMessage(
				chatId = watcher.chatId,
				text = generateMatchMessage(blueprint, product),
				url = AsyncMessageQueue.Url(
					description = "Go to the listing",
					url = "https://www.cardtrader.com/cards/${blueprint.slug}"
				)
			)
		}

		watchers.forEach {
			if (it.triggered) {
				couchDbService.createOrUpdateWatcher(it.copy(triggered = false))
			}
		}
		activatedWatchers.map { it.second }.forEach {
			if (!it.triggered) {
				couchDbService.createOrUpdateWatcher(it.copy(triggered = true))
			}
		}
	}

	private fun Watcher.matches(product: Product): Boolean =
		product.priceCents <= priceThreshold
			&& product.properties.condition in conditions
			&& product.properties.language in languages
			&& (!cardTraderZeroOnly || product.user.canSellViaHub)

	private fun generateMatchMessage(blueprint: Blueprint, product: Product) = buildString {
		append("New listing for ${blueprint.name}:\n")
		append("Condition: ${product.properties.condition.serialName}\n")
		append("Language: ${product.properties.language.toEmoji()}\n")
		append("Price: ${product.priceCents/100.0} €")
	}

	private fun MtgLanguage.toEmoji(): String = when(this) {
		MtgLanguage.De -> "🇩🇪"
		MtgLanguage.En -> "🇬🇧"
		MtgLanguage.Es -> "🇪🇸"
		MtgLanguage.Fr -> "🇫🇷"
		MtgLanguage.It -> "🇮🇹"
		MtgLanguage.Jp -> "🇯🇵"
		MtgLanguage.Pt -> "🇵🇹"
	}

}