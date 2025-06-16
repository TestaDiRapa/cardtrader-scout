package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun AlertBlock(
	backgroundColor: CSSColorValue,
	title: String,
	text: String,
	textColor: CSSColorValue,
) {
	Div({
		style {
			width(95.percent)
			padding(16.px)
			backgroundColor(backgroundColor)
			borderRadius(8.px)
			textAlign("center")
		}
	}) {
		H3({
			style {
				color(textColor)
				fontWeight("bold")
			}
		}) {
			Text(title)
		}

		Div({
			style {
				marginTop(16.px)
				color(textColor)
				fontSize(16.px)
			}
		}) {
			Text(text)
		}
	}
}
