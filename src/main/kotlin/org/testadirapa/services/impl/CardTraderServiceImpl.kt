package org.testadirapa.services.impl

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.exclude
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
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
import org.testadirapa.services.CardTraderService
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration.Companion.hours

class CardTraderServiceImpl(
	cardTraderToken: String
): CardTraderService {

	private val serviceScope = CoroutineScope(Dispatchers.Default)

	private val client = HttpConfig.newHttpClient {
		defaultRequest {
			url("https://api.cardtrader.com/api/v2/")
			bearerAuth(cardTraderToken)
		}
	}

	private val cachedExpansionsByCode: AtomicReference<Deferred<Pair<Long, Map<String, Expansion>>>?> = AtomicReference(null)

	override tailrec suspend fun getExpansionByCode(code: String): Expansion? {
		val currentJob = cachedExpansionsByCode.get()
		val expansions = currentJob?.await()
		return if (expansions != null && expansions.first > (System.currentTimeMillis() - 1.hours.inWholeMilliseconds)) {
			expansions.second[code]
		} else {
			val newJob = serviceScope.async(start = CoroutineStart.LAZY) {
				val result = client.get {
					url {
						appendPathSegments("expansions")
					}
				}.body<List<Expansion>>().associateBy { it.code }
				System.currentTimeMillis() to result
			}
			if (!cachedExpansionsByCode.compareAndSet(currentJob, newJob)) {
				newJob.cancel()
			}
			getExpansionByCode(code)
		}
	}

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
		blueprintId: String,
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
	}.body()
}