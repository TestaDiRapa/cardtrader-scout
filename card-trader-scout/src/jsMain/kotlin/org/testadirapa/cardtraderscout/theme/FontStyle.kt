package org.testadirapa.cardtraderscout.theme

import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.fontFamily

object FontStyles : StyleSheet() {
	val fontFamily by style {
		fontFamily(
			"-apple-system", "BlinkMacSystemFont", "\"Segoe UI\"",
			"Roboto", "Helvetica", "Arial", "sans-serif"
		)
	}
}