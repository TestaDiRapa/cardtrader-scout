package org.testadirapa.telegram.states.removewatcher

import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.behaviour_builder.DefaultBehaviourContextWithFSM
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.webAppButton
import dev.inmo.tgbotapi.types.BotCommand
import dev.inmo.tgbotapi.types.LinkPreviewOptions
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.types.webapps.WebAppInfo
import dev.inmo.tgbotapi.utils.row
import org.testadirapa.telegram.states.StatefulBotCommand
import org.testadirapa.telegram.states.BotCommandRegistrar
import org.testadirapa.telegram.states.BotState
import org.testadirapa.telegram.states.ctx.Context

data class ManageWatchersState(
	override val context: Context
) : BotState {

	companion object : BotCommandRegistrar, StatefulBotCommand<DefaultBehaviourContextWithFSM<BotState>>() {
		override val command: String = "manage"

		override suspend fun DefaultBehaviourContextWithFSM<BotState>.initializer(
			message: TextMessage,
			webAppUrl: String
		) {
			startChain(ManageWatchersState(Context(message.chat.id, webAppUrl)))
		}

		override suspend fun DefaultBehaviourContextWithFSM<BotState>.registerStates() {
			registerState()
		}

		context(DefaultBehaviourContextWithFSM<BotState>)
		override suspend fun registerState() {
			strictlyOn<ManageWatchersState> {
				send(
					chatId = it.context.chatId,
					replyMarkup = inlineKeyboard {
						row {
							webAppButton("Open WebApp", WebAppInfo("${it.context.webAppUrl}?op=manage"))
						}
					},
					linkPreviewOptions = LinkPreviewOptions.Small(
						"${it.context.webAppUrl}?op=manage",
						showAboveText = false
					)
				) {
					+"Open the URL to edit your existing watchers"
				}
				null
			}
		}

		override fun getDescription(): BotCommand =
			BotCommand("manage", "Manage your existing watchers")
	}
}