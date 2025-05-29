package org.testadirapa.models.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CardCondition {
	@SerialName("Mint") Mint,
	@SerialName("Near Mint") NearMint,
	@SerialName("Slightly Played") SlightlyPlayed,
	@SerialName("Moderately Played") ModeratelyPlayed,
	@SerialName("Played") Played,
	@SerialName("Heavily Played") HeavilyPlayed,
	@SerialName("Poor") Poor
}