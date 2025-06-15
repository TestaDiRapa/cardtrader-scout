package org.testadirapa.telegram.states.ctx

import dev.inmo.tgbotapi.types.IdChatIdentifier

data class Context(
	val chatId: IdChatIdentifier,
	val webAppUrl: String
)