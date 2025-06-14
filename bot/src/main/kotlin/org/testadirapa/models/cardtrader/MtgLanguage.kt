package org.testadirapa.models.cardtrader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MtgLanguage {
	@SerialName("de") De,
	@SerialName("en") En,
	@SerialName("es") Es,
	@SerialName("fr") Fr,
	@SerialName("it") It,
	@SerialName("jp") Jp,
	@SerialName("pt") Pt
}