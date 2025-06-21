package org.testadirapa.cardtrader

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
	@SerialName("pt") Pt,
	@SerialName("zh-CN") Cn
}

fun MtgLanguage.toEmoji(): String = when(this) {
	MtgLanguage.De -> "🇩🇪"
	MtgLanguage.En -> "🇬🇧"
	MtgLanguage.Es -> "🇪🇸"
	MtgLanguage.Fr -> "🇫🇷"
	MtgLanguage.It -> "🇮🇹"
	MtgLanguage.Jp -> "🇯🇵"
	MtgLanguage.Pt -> "🇵🇹"
	MtgLanguage.Cn -> "🇨🇳"
}