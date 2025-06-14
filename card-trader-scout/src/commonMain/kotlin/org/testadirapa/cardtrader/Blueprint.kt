package org.testadirapa.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Blueprint(
	val id: Long,
	@SerialName("expansion_id") val expansionId: Int,
	val slug: String,
	val name: String,
	val version: String? = null,
	val image: Image? = null,
	@SerialName("back_image") val backImage: Image? = null
)