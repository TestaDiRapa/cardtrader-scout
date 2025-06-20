package org.testadirapa.components

import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.toChatId
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import kotlin.math.log

object AsyncMessageQueue {

	private val logger = LoggerFactory.getLogger(AsyncMessageQueue::class.java)

	data class Url(val description: String, val url: String)

	data class Message(
		val chatId: ChatId,
		val text: String,
		val url: Url? = null,
		val imageUrl: String? = null,
	)
	private val messageChannel = Channel<Message>()
	private val errorChannel = Channel<Exception>()

	suspend fun sendMessage(
		chatId: Long,
		text: String,
		url: Url? = null,
		imageUrl: String? = null,
	) {
		messageChannel.send(
			Message(
				chatId = chatId.toChatId(),
				text = text,
				url = url,
				imageUrl = imageUrl,
			)
		)
	}

	suspend fun onNewMessage(block: suspend (Message) -> Unit) {
		for (message in messageChannel) {
			try {
				block(message)
			} catch (e: Exception) {
				errorChannel.send(e)
			}
		}
	}

	suspend fun sendError(exception: Exception) {
		errorChannel.send(exception)
	}

	suspend fun onNewException(block: suspend (Exception) -> Unit) {
		for (exception in errorChannel) {
			try {
				block(exception)
			} catch (e: Exception) {
				logger.error("Error in error queue", e)
			}
		}
	}

}