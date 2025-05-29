package org.testadirapa.models.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Expansion(
	val id: Int,
	@SerialName("game_id") val gameId: Int,
	val code: String,
	val name: String,
)