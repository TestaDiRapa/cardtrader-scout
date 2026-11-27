package org.testadirapa.cardtrader

import kotlinx.serialization.Serializable

@Serializable
data class Price(
	val cents: Int,
	val currency: Currency,
)