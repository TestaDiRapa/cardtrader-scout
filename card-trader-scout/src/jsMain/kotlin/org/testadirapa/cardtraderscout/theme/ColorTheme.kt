package org.testadirapa.cardtraderscout.theme

import dev.inmo.tgbotapi.webapps.ColorScheme
import dev.inmo.tgbotapi.webapps.WebApp
import dev.inmo.tgbotapi.webapps.colorScheme
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color

object ColorTheme {

	data class ColorSchemeParams(
		val errorBackground: CSSColorValue,
		val textColor: CSSColorValue,
		val borderColor: CSSColorValue,
		val backgroundColor: CSSColorValue
	)

	fun getColorScheme(webApp: WebApp): ColorSchemeParams = when (webApp.colorScheme) {
		ColorScheme.LIGHT -> ColorSchemeParams(
			errorBackground = Color("#FF9B9B"),
			textColor = Color(webApp.themeParams.textColor ?: "#000000"),
			borderColor = Color("#8f8f9d"),
			backgroundColor = Color(webApp.themeParams.backgroundColor ?: "#141218")
		)
		ColorScheme.DARK -> ColorSchemeParams(
			errorBackground = Color("#6B2424"),
			textColor = Color(webApp.themeParams.textColor ?: "#FFFFFF"),
			borderColor = Color("#c4c4cc"),
			backgroundColor = Color(webApp.themeParams.backgroundColor ?: "#FEF7FF")
		)
	}
}