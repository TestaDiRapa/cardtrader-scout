package org.testadirapa.services.impl

import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.appendPathSegments
import io.ktor.http.contentLength
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.testadirapa.http.HttpConfig
import org.testadirapa.models.cardtrader.Blueprint
import org.testadirapa.models.cardtrader.Expansion
import org.testadirapa.models.cardtrader.MtgLanguage
import org.testadirapa.models.cardtrader.Product
import org.testadirapa.models.cardtrader.exception.NotFoundException
import org.testadirapa.services.CardTraderService
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration.Companion.hours

class CardTraderServiceImpl private constructor(
	cardTraderToken: String
): CardTraderService {

	companion object {

		private lateinit var instance: CardTraderServiceImpl
		operator fun invoke(token: String): CardTraderServiceImpl {
			if (!::instance.isInitialized) {
				instance = CardTraderServiceImpl(token)
			}
			return instance
		}

		operator fun invoke(): CardTraderServiceImpl {
			if (!::instance.isInitialized) {
				throw IllegalStateException("CardTraderServiceImpl is not initialized")
			}
			return instance
		}

	}

	data class CachedExpansions(
		private val expansionsById: Map<Int, Expansion>,
	) {
		private val idsByCode: Map<String, Int> = expansionsById.values.associate {
			it.code to it.id
		}
		operator fun get(id: Int): Expansion? = expansionsById[id]
		operator fun get(code: String): Expansion? = idsByCode[code]?.let { expansionsById[it] }

	}

	private val serviceScope = CoroutineScope(Dispatchers.Default)

	private val client = HttpConfig.newHttpClient {
		defaultRequest {
			url("https://api.cardtrader.com/api/v2/")
			bearerAuth(cardTraderToken)
		}
	}

	private val cachedExpansions: AtomicReference<Deferred<Pair<Long, CachedExpansions>>?> = AtomicReference(null)

	private tailrec suspend fun getExpansions(): CachedExpansions {
		val currentJob = cachedExpansions.get()
		val expansions = currentJob?.await()
		return if (expansions != null && expansions.first > (System.currentTimeMillis() - 1.hours.inWholeMilliseconds)) {
			expansions.second
		} else {
			val newJob = serviceScope.async(start = CoroutineStart.LAZY) {
				val result = client.get {
					url {
						appendPathSegments("expansions")
					}
				}.body<List<Expansion>>().let {
					CachedExpansions(it.associateBy { it.id })
				}
				System.currentTimeMillis() to result
			}
			if (!cachedExpansions.compareAndSet(currentJob, newJob)) {
				newJob.cancel()
			}
			getExpansions()
		}
	}

	override suspend fun getExpansionByCode(code: String): Expansion? = getExpansions()[code]

	override suspend fun getExpansionById(id: Int): Expansion? = getExpansions()[id]

	override suspend fun searchBlueprintsByNameAndExpansionId(name: String, expansionId: Int?): List<Blueprint> =
		client.get {
			url {
				appendPathSegments("blueprints")
				parameter("name", name)
				if (expansionId != null) {
					parameter("expansion_id", expansionId)
				}
			}
		}.body()

	override suspend fun searchProducts(
		blueprintId: Long,
		expansionId: String?,
		foil: Boolean,
		language: MtgLanguage?
	): List<Product> = client.get {
		url {
			appendPathSegments("marketplace", "products")
			parameter("blueprint_id", blueprintId)
			if (expansionId != null) {
				parameter("expansion_id", expansionId)
			}
			parameter("foil", foil)
			if (language != null) {
				parameter("language", language)
			}
		}
	}.body<Map<String, List<Product>>>().let {
		if (!it.contains(blueprintId.toString())) {
			throw NotFoundException("Blueprint $blueprintId not found in products")
		}
		it.getValue(blueprintId.toString())
	}

	override suspend fun getImage(url: String) =
		client.get {
			url {
				takeFrom("https://cardtrader.com")
				appendPathSegments(url)
			}
		}.let {
			it.bodyAsChannel() to it.contentLength()
		}

}