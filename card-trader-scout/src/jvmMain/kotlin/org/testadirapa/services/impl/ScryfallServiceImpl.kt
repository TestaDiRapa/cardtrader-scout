package org.testadirapa.services.impl

import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import org.testadirapa.http.HttpConfig
import org.testadirapa.scryfall.PaginatedList
import org.testadirapa.scryfall.ScryfallCard
import org.testadirapa.services.ScryfallService

class ScryfallServiceImpl private constructor(): ScryfallService {

	companion object {

		private lateinit var instance: ScryfallServiceImpl

		operator fun invoke(): ScryfallServiceImpl {
			if (!::instance.isInitialized) {
				instance = ScryfallServiceImpl()
			}
			return instance
		}

	}

	private val client = HttpConfig.newHttpClient {
		defaultRequest {
			url("https://api.scryfall.com")
		}
	}

	override suspend fun searchCards(query: String): PaginatedList<ScryfallCard> {
		require(query.length >= 3) { "Search query must have at least 3 characters" }
		require(query.length < 1000) { "Search query cannot exceed 1000 characters" }
		return client.get {
			url {
				appendPathSegments("cards", "search")
				parameter("q", query)
			}
		}.body()
	}

}