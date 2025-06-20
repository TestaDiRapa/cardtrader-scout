package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

@Composable
fun Spinner() {
	Div(attrs = {
		classes("spinner-container")
	}) {
		Div(attrs = {
			classes("spinner")
			style {
				marginTop(8.px)
			}
		})
	}
}

