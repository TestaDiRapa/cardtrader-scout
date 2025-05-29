package org.testadirapa.models.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductProperties(
	val condition: CardCondition = CardCondition.NearMint,
	@SerialName("mtg_foil") val foil: Boolean = false,
	@SerialName("mtg_language") val language: MtgLanguage = MtgLanguage.En,
)