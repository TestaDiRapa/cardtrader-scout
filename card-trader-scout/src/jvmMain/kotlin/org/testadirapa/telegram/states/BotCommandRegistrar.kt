package org.testadirapa.telegram.states

import dev.inmo.tgbotapi.extensions.behaviour_builder.DefaultBehaviourContextWithFSM
import dev.inmo.tgbotapi.types.BotCommand

interface BotCommandRegistrar {

	context(DefaultBehaviourContextWithFSM<BotState>)
	suspend fun registerState()

	fun getDescription(): BotCommand?

}