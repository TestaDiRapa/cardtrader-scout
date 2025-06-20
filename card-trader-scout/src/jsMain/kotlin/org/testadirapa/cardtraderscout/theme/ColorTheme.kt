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
		val backgroundColor: CSSColorValue,
		val warningBackground: CSSColorValue,
		val warningText: CSSColorValue,
		val selectedBackground: CSSColorValue,
		val unSelectedBackground: CSSColorValue,
	)

	fun getColorScheme(webApp: WebApp): ColorSchemeParams = when (webApp.colorScheme) {
		ColorScheme.LIGHT -> ColorSchemeParams(
			errorBackground = Color("#FF9B9B"),
			textColor = Color(webApp.themeParams.textColor ?: "#000000"),
			borderColor = Color("#8f8f9d"),
			backgroundColor = Color(webApp.themeParams.backgroundColor ?: "#141218"),
			warningBackground = Color("#FFF8D1"),
			warningText = Color("#856404"),
			selectedBackground = Color("#B2DFDB"),
			unSelectedBackground = Color("#F1F3F4"),
		)
		ColorScheme.DARK -> ColorSchemeParams(
			errorBackground = Color("#6B2424"),
			textColor = Color(webApp.themeParams.textColor ?: "#FFFFFF"),
			borderColor = Color("#c4c4cc"),
			backgroundColor = Color(webApp.themeParams.backgroundColor ?: "#FEF7FF"),
			warningBackground = Color("#FFF8D1"),
			warningText = Color("#856404"),
			selectedBackground = Color("#2D9CDB"),
			unSelectedBackground = Color("#2A2E35"),
		)
	}
}