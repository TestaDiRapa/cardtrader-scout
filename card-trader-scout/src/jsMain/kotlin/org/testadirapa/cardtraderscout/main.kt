package org.testadirapa.cardtraderscout

import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.testadirapa.cardtraderscout.theme.FontStyles

fun main() {
	renderComposable("root") {
		Style(FontStyles)
		App()
	}
}