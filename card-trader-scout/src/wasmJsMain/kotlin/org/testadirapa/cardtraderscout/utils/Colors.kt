package org.testadirapa.cardtraderscout.utils

import androidx.compose.ui.graphics.Color
import org.testadirapa.cardtraderscout.telegram.webapp.HEXColor

fun HEXColor.toColor(): Color {
	val red = substring(1, 3).toInt(16)
	val green = substring(3, 5).toInt(16)
	val blue = substring(5, 7).toInt(16)

	return Color(red = red, green = green, blue = blue)
}