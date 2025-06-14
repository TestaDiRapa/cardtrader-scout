package org.testadirapa.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CardCondition(val serialName: String) {
	@SerialName("Mint") Mint("Mint"),
	@SerialName("Near Mint") NearMint("Near Mint"),
	@SerialName("Slightly Played") SlightlyPlayed("Slightly Played"),
	@SerialName("Moderately Played") ModeratelyPlayed("Moderately Played"),
	@SerialName("Played") Played("Played"),
	@SerialName("Heavily Played") HeavilyPlayed("Heavily Played"),
	@SerialName("Poor") Poor("Poor")
}