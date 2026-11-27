package org.testadirapa.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
	val id: Int,
	val price: Price,
	@SerialName("properties_hash") val properties: ProductProperties,
	@SerialName("on_vacation") val onVacation: Boolean = false,
	val user: Seller
)