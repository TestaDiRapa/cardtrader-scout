package org.testadirapa.telegram.states

import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.behaviour_builder.DefaultBehaviourContextWithFSM
import dev.inmo.tgbotapi.types.IdChatIdentifier

data class ErrorState(override val context: IdChatIdentifier, val message: String) : BotState {

	companion object : BotCommandRegistrar {
		context(DefaultBehaviourContextWithFSM<BotState>)
		override suspend fun registerState() {
			strictlyOn<ErrorState> {
				send(it.context) { +"Error ${it.message}" }
				null
			}
		}
	}

}