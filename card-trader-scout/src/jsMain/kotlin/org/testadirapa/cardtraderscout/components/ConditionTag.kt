package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun ConditionTag(
	text: String,
	color: CSSColorValue,
	size: CSSSizeValue<CSSUnit.px> = 32.px,
	fontSize: CSSSizeValue<CSSUnit.px> = 14.px,
	marginRight: CSSSizeValue<CSSUnit.px>? = null,
) {
	Div({
		style {
			width(size)
			height(size)
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			justifyContent(JustifyContent.Center)
			backgroundColor(color)
			color(Color.white)
			fontSize(fontSize)
			borderRadius(4.px)
			if (marginRight != null) {
				marginRight(marginRight)
			}
		}
	}) {
		Text(text)
	}
}