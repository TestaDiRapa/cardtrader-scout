package org.testadirapa.telegram

import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndFSMAndStartLongPolling
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.urlButton
import dev.inmo.tgbotapi.requests.abstracts.InputFile
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.testadirapa.components.AsyncMessageQueue
import org.testadirapa.telegram.states.BotState
import org.testadirapa.telegram.states.ErrorState
import org.testadirapa.telegram.states.addwatcher.AddWatcherState

class CardTraderScoutBot(
	private val webAppUrl: String,
	private val telegramApiKey: String
) {
	private val botScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
	private val logger = LoggerFactory.getLogger(CardTraderScoutBot::class.java)

	fun BehaviourContext.listenForAsyncMessages() = botScope.launch {
		AsyncMessageQueue.onNewMessage { message ->
			if (message.imageUrl != null) {
				sendPhoto(
					chatId = message.chatId,
					fileId = InputFile.fromUrl(message.imageUrl),
					text = message.text,
					replyMarkup = message.url?.let { url ->
						inlineKeyboard {
							row {
								urlButton(text = url.description, url = url.url)
							}
						}
					},
				)
			} else {
				sendMessage(
					chatId = message.chatId,
					text = message.text,
					replyMarkup = message.url?.let { url ->
						inlineKeyboard {
							row {
								urlButton(text = url.url, url = url.url)
							}
						}
					},
				)
			}
		}
	}

	suspend fun start() {
		telegramBotWithBehaviourAndFSMAndStartLongPolling<BotState>(
			telegramApiKey,
			CoroutineScope(Dispatchers.IO),
			onStateHandlingErrorHandler = { state, e ->
				when (state) {
					is AddWatcherState -> {
						logger.error("Error in AddWatcher state", e)
						ErrorState(state.context.chatId, e.message ?: e.localizedMessage)
					}
					is ErrorState -> {
						logger.error("Error in error state", e)
						state
					}
					else -> {
						logger.error("Error in unknown state", e)
						null
					}
				}
			}
		) {
			listenForAsyncMessages()
			ErrorState.registerState()
			AddWatcherState.register(webAppUrl)
			setMyCommands(
				listOfNotNull(
					ErrorState.getDescription(),
					AddWatcherState.getDescription()
				)
			)
		}.second.join()
	}

}