package org.testadirapa.cardtraderscout.utils

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.testadirapa.cardtrader.CardCondition

fun CardCondition.menuOptions(): Triple<String, CSSColorValue, String> = when (this) {
	CardCondition.Mint -> Triple("Mint", Color("#7BB125"), "MI")
	CardCondition.NearMint -> Triple("Near Mint", Color("#7BB125"), "NM")
	CardCondition.SlightlyPlayed -> Triple("Slightly Played", Color("#A5B200"), "SP")
	CardCondition.ModeratelyPlayed -> Triple("Moderately Played", Color("#E39101"), "MP")
	CardCondition.Played -> Triple("Played", Color("#FD8428"), "PL")
	CardCondition.HeavilyPlayed -> Triple("Heavily Played", Color("#9C3333"), "HP")
	CardCondition.Poor -> Triple("Poor", Color("#9C3333"), "PO")
}