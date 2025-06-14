package org.testadirapa.telegram.states

import dev.inmo.tgbotapi.extensions.behaviour_builder.DefaultBehaviourContextWithFSM

interface BotCommandRegistrar {

	context(DefaultBehaviourContextWithFSM<BotState>)
	suspend fun registerState()

}