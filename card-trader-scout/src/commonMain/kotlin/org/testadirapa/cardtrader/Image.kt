package org.testadirapa.cardtrader

import kotlinx.serialization.Serializable

@Serializable
data class Image(
	val url: String,
	val show: Url,
	val preview: Url,
	val social: Url
)