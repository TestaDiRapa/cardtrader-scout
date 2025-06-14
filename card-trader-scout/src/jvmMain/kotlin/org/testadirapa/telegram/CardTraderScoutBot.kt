package org.testadirapa.telegram

import dev.inmo.micro_utils.coroutines.subscribeLoggingDropExceptions
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndFSMAndStartLongPolling
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.urlButton
import dev.inmo.tgbotapi.utils.TelegramAPIUrlsKeeper
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.testadirapa.components.AsyncMessageQueue
import org.testadirapa.telegram.states.BotState
import org.testadirapa.telegram.states.ErrorState
import org.testadirapa.telegram.states.addwatcher.AddWatcherState

class CardTraderScoutBot(
	private val telegramApiKey: String
) {
	private val botScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

	fun BehaviourContext.listenForAsyncMessages() = botScope.launch {
		AsyncMessageQueue.onNewMessage { message ->
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

	suspend fun start() {
		telegramBotWithBehaviourAndFSMAndStartLongPolling<BotState>(
			telegramApiKey,
			CoroutineScope(Dispatchers.IO),
			onStateHandlingErrorHandler = { state, e ->
				when (state) {
					is AddWatcherState -> {
						ErrorState(state.context, e.message ?: e.localizedMessage)
					}
					is ErrorState -> {
						state
					}
					else -> {
						println("Error in unknown state: ${e.stackTraceToString()}")
						null
					}
				}
			}
		) {
			listenForAsyncMessages()
			ErrorState.registerState()
			AddWatcherState.register()


			allUpdatesFlow.subscribeLoggingDropExceptions(scope = this) {
				println(it)
			}
		}.second.join()
	}

}