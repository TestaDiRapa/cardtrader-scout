package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.CSSpxValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun Title(
	colorScheme: ColorTheme.ColorSchemeParams,
	text: String,
	size: CSSSizeValue<CSSUnit.px> = 24.px,
	marginBottom: CSSSizeValue<CSSUnit.px> = 4.px,
) {
	Div({
		style {
			textAlign("center")
			width(100.percent)
			marginBottom(marginBottom)
			paddingTop(0.px)
			marginTop(0.px)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
		}
	}) {
		H3({
			style {
				fontSize(size)
				color(colorScheme.textColor)
			}
		}) {
			Text(text)
		}
	}
}