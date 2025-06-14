package org.testadirapa.models.scryfall

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardImagery(
	val small: String,
	val normal: String,
	val large: String,
	val png: String,
	@SerialName("art_crop") val artCrop: String,
	@SerialName("border_crop") val borderCrop: String,
)