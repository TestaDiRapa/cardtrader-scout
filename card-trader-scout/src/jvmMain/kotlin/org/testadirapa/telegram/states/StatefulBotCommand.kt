package org.testadirapa.telegram.states

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.command
import dev.inmo.tgbotapi.types.message.content.TextMessage

abstract class StatefulBotCommand<T : BehaviourContext> {

	protected abstract val command: String

	protected abstract suspend fun T.initializer(message: TextMessage, webAppUrl: String)

	protected abstract suspend fun T.registerStates()

	context(T)
	suspend fun register(webAppUrl: String) {
		command(command) { initializer(it, webAppUrl) }
		registerStates()
	}

}