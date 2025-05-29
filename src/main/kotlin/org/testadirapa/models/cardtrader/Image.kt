package org.testadirapa.models.cardtrader

import kotlinx.serialization.Serializable

@Serializable
data class Image(
	val url: String,
	val show: Url,
	val preview: Url,
	val social: Url
)