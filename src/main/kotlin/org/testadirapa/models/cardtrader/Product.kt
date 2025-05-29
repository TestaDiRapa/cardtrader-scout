package org.testadirapa.models.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
	val id: Int,
	@SerialName("price_cents") val priceCents: Int,
	@SerialName("price_currency") val currency: Currency,
	@SerialName("properties_hash") val properties: ProductProperties,
	@SerialName("on_vacation") val onVacation: Boolean,
	val user: Seller
)