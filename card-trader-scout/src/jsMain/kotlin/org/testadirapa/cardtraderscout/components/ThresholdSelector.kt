package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtraderscout.components.styles.textInputStyle
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun ThresholdSelector(
	colorScheme: ColorTheme.ColorSchemeParams,
	onClick: (Double, Boolean) -> Unit,
) {
	var amount by remember { mutableStateOf("") }
	var fastShipping by remember { mutableStateOf(false) }

	Title(
		colorScheme = colorScheme,
		text = "Receive a notification if the price drops under:",
		size = 18.px
	)

	Input(type = InputType.Text) {
		value(amount)
		onInput {
			val value = it.value
			if (value.matches(Regex("^\\d*[.,]?\\d{0,2}$"))) {
				amount = value
					.replace(",", ".")
					.replace(Regex("^0+(\\d+\\.?\\d{0,2})$"), "$1")
			}
		}
		attr("inputmode", "decimal")
		placeholder("0.42 €")
		textInputStyle(colorScheme)
	}

	Div({
		style {
			marginTop(16.px)
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			color(colorScheme.textColor)
		}
	}) {
		Input(type = InputType.Checkbox) {
			checked(fastShipping)
			onChange { fastShipping = it.value }

			style {
				height(36.px)
				backgroundColor(if (fastShipping) Color("#6c667a") else Color("#2c2f36"))
				border {
					width = 2.px
					color(Color("#888"))
					style = LineStyle.Solid
				}
				borderRadius(12.px)
				position(Position.Relative)
				cursor("pointer")
			}
		}
		Text("CardTraderZero only")
	}

	FloatingMainButton(
		text = "Confirm",
		show = amount.toDoubleOrNull()?.let { it > 0.0 } ?: false
	) { onClick(amount.toDouble(), fastShipping) }
}
