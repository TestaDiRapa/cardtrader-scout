package org.testadirapa.components

import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.toChatId
import kotlinx.coroutines.channels.Channel

object AsyncMessageQueue {

	data class Url(val description: String, val url: String)

	data class Message(
		val chatId: ChatId,
		val text: String,
		val url: Url? = null,
	)
	private val messageChannel = Channel<Message>()

	suspend fun sendMessage(
		chatId: Long,
		text: String,
		url: Url? = null,
	) {
		messageChannel.send(
			Message(
				chatId = chatId.toChatId(),
				text = text,
				url = url,
			)
		)
	}

	suspend fun onNewMessage(block: suspend (Message) -> Unit) {
		for (message in messageChannel) {
			block(message)
		}
	}

}