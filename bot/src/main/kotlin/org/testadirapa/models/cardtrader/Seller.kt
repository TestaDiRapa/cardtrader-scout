package org.testadirapa.models.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Seller(
	val id: Int,
	val username: String,
	@SerialName("can_sell_via_hub") val canSellViaHub: Boolean
)