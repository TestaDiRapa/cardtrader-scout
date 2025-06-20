package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun AlertBlock(
	backgroundColor: CSSColorValue,
	title: String? = null,
	text: String,
	textColor: CSSColorValue,
	marginTop: CSSNumericValue<out CSSUnit>? = null,
) {
	Div({
		style {
			width(90.percent)
			padding(16.px)
			backgroundColor(backgroundColor)
			borderRadius(8.px)
			marginLeft(4.px)
			textAlign("center")
			if (marginTop != null) {
				marginTop(marginTop)
			}
		}
	}) {
		if (title != null) {
			H3({
				style {
					color(textColor)
					fontWeight("bold")
					marginBottom(16.px)
				}
			}) {
				Text(title)
			}
		}
		Div({
			style {

				color(textColor)
				fontSize(16.px)
			}
		}) {
			Text(text)
		}
	}
}
