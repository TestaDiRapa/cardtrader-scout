package org.testadirapa.cardtraderscout.components.styles

import org.jetbrains.compose.web.attributes.builders.InputAttrsScope
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.css.width
import org.testadirapa.cardtraderscout.theme.ColorTheme

fun InputAttrsScope<String>.textInputStyle(
	colorScheme: ColorTheme.ColorSchemeParams,
	block: (StyleScope.() -> Unit)? = null
) {
	style {
		flexGrow(1)
		fontSize(16.px)
		lineHeight(48.px)
		paddingLeft(8.px)
		color(colorScheme.textColor)
		backgroundColor(colorScheme.backgroundColor)
		border {
			style(LineStyle.Solid)
			width(1.px)
			color(colorScheme.borderColor)
		}
		borderRadius(6.px)
		width(100.percent)
		boxSizing("border-box")
		if (block != null) {
			block()
		}
	}
}