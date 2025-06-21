package org.testadirapa

import dev.inmo.tgbotapi.types.toChatId
import dev.inmo.tgbotapi.utils.TelegramAPIUrlsKeeper
import org.testadirapa.components.PriceChecker
import org.testadirapa.services.impl.CardTraderServiceImpl
import org.testadirapa.services.impl.CouchDbServiceImpl
import org.testadirapa.services.impl.ScryfallServiceImpl
import org.testadirapa.telegram.CardTraderScoutBot
import org.testadirapa.webview.WebviewServer

suspend fun main() {
	val webAppPath = System.getenv("WEBAPP_FOLDER")
	val webAppUrl = System.getenv("WEBAPP_URL")
	val telegramApiKey = System.getenv("TELEGRAM_API_KEY")
	val cardTraderToken = System.getenv("CARD_TRADER_TOKEN")
	val logChat = System.getenv("LOG_CHAT").toLong().toChatId()
	val scryfallService = ScryfallServiceImpl()
	val cardTraderService = CardTraderServiceImpl(cardTraderToken)
	val couchDbService = CouchDbServiceImpl(
		couchDbUrl = System.getenv("COUCHDB_URL"),
		username = System.getenv("COUCHDB_USERNAME"),
		password = System.getenv("COUCHDB_PASSWORD"),
	)
	PriceChecker(cardTraderService, couchDbService).launchCheckJob()
	WebviewServer(
		webAppStaticFolder = webAppPath,
		cardTraderService = cardTraderService,
		scryfallService = scryfallService,
		couchDbService = couchDbService,
		urlKeeper = TelegramAPIUrlsKeeper(
			token = telegramApiKey,
			testServer = false
		)
	).startServer()
	CardTraderScoutBot(
		logChat = logChat,
		webAppUrl = webAppUrl,
		telegramApiKey = telegramApiKey
	).start()
}