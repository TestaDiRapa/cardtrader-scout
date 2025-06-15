package org.testadirapa.cardtraderscout.pages

import androidx.compose.runtime.Composable
import dev.inmo.tgbotapi.webapps.HEXColor
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun AlertBlock(
	backgroundColor: HEXColor,
	title: String,
	text: String,
	textColor: HEXColor
) {
	Div({
		style {
			width(95.percent)
			padding(16.px)
			backgroundColor(Color(backgroundColor))
			borderRadius(8.px)
			textAlign("center")
		}
	}) {
		H3({
			style {
				color(Color(textColor))
				fontWeight("bold")
			}
		}) {
			Text(title)
		}

		Div({
			style {
				marginTop(16.px)
				color(Color(textColor))
				fontSize(16.px)
			}
		}) {
			Text(text)
		}
	}
}
