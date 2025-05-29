package org.testadirapa

import kotlinx.coroutines.runBlocking
import org.testadirapa.services.impl.CardTraderServiceImpl
import org.testadirapa.services.impl.ScryfallServiceImpl

fun main() {
	runBlocking {
		val token = System.getenv("CARD_TRADER_TOKEN")
		val scryfallService = ScryfallServiceImpl()
		val cardTraderService = CardTraderServiceImpl(token)
		val card = scryfallService.searchCards("Galadriel").data.first()
		println(cardTraderService.getExpansionByCode(card.setCode))
	}
}