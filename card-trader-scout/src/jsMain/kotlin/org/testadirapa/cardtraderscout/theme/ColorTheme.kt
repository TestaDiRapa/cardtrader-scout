package org.testadirapa.cardtraderscout.theme

import dev.inmo.tgbotapi.webapps.ColorScheme
import dev.inmo.tgbotapi.webapps.HEXColor
import dev.inmo.tgbotapi.webapps.WebApp
import dev.inmo.tgbotapi.webapps.colorScheme

object ColorTheme {

	data class ColorSchemeParams(
		val errorBackground: HEXColor,
		val textColor: HEXColor
	)

	fun getColorScheme(webApp: WebApp): ColorSchemeParams = when (webApp.colorScheme) {
		ColorScheme.LIGHT -> ColorSchemeParams(
			errorBackground = "#FF9B9B",
			textColor = webApp.themeParams.textColor ?: "#000000"
		)
		ColorScheme.DARK -> ColorSchemeParams(
			errorBackground = "#6B2424",
			textColor = webApp.themeParams.textColor ?: "#FFFFFF"
		)
	}
}