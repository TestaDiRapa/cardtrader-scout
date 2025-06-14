package org.testadirapa.scryfall

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScryfallCard(
	val id: String,
	val name: String,
	@SerialName("oracle_id") val oracleId: String? = null,
	val lang: String,
	@SerialName("image_uris") val imageUris: CardImagery? = null,
	@SerialName("set") val setCode: String,
	@SerialName("set_name") val setName: String,
) : ScryfallRootEntity