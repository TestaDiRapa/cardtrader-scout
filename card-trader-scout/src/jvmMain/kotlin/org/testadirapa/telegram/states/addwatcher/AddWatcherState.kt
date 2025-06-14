package org.testadirapa.telegram.states.addwatcher

import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.behaviour_builder.DefaultBehaviourContextWithFSM
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.urlButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.webAppButton
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.LinkPreviewOptions
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.types.webapps.WebAppInfo
import dev.inmo.tgbotapi.utils.row
import org.testadirapa.components.InteractionCache
import org.testadirapa.telegram.states.StatefulBotCommand
import org.testadirapa.telegram.states.BotCommandRegistrar
import org.testadirapa.telegram.states.BotState
import java.util.UUID

data class AddWatcherState(
	override val context: IdChatIdentifier
) : BotState {

	companion object : BotCommandRegistrar, StatefulBotCommand<DefaultBehaviourContextWithFSM<BotState>>() {
		override val command: String = "new"

		override suspend fun DefaultBehaviourContextWithFSM<BotState>.initializer(message: TextMessage) {
			startChain(AddWatcherState(message.chat.id))
		}

		override suspend fun DefaultBehaviourContextWithFSM<BotState>.registerStates() {
			registerState()
		}

		val url = "https://ab29-2a02-a03f-65ab-de01-3c12-c2aa-9e91-9446.ngrok-free.app?op=new"

		context(DefaultBehaviourContextWithFSM<BotState>)
		override suspend fun registerState() {
			strictlyOn<AddWatcherState> {
				val token = UUID.randomUUID().toString()
				InteractionCache.addInteraction(it.context, token)
				send(
					chatId = it.context,
					replyMarkup = inlineKeyboard {
						row {
							webAppButton("Open WebApp", WebAppInfo(url))
						}
					},
					linkPreviewOptions = LinkPreviewOptions.Small(
						url,
						showAboveText = false
					)
				) {
					+"Open the URL to watch the price for a new card"
				}
				null
			}
		}
	}
}