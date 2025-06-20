package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID

@Composable
fun ConditionSelector(
	colorScheme: ColorTheme.ColorSchemeParams,
	onChoose: (Set<CardCondition>) -> Unit,
) {
	var selectedItemIds by remember { mutableStateOf<Set<String>>(emptySet()) }
	var selectedItems by remember { mutableStateOf<Set<CardCondition>>(emptySet()) }

	H3({
		style {
			textAlign("center")
			marginBottom(8.px)
			color(colorScheme.textColor)
		}
	}) { Text("Choose the condition(s)") }

	ConditionRow(
		colorScheme = colorScheme,
		text = "Any condition",
		shortName = "NA",
		color = Color.lightgray,
		isSelected = selectedItemIds.contains(ALL_INTERNAL_ID),
		onClick = {
			selectedItemIds = setOf(ALL_INTERNAL_ID)
			selectedItems = CardCondition.entries.toSet()
		}
	)

	CardCondition.entries.forEach { condition ->
		val (name, color, shortName) = condition.menuOptions()
		val isSelected = selectedItemIds.contains(condition.name)

		ConditionRow(
			colorScheme = colorScheme,
			text = name,
			shortName = shortName,
			color = color,
			isSelected = isSelected,
			onClick = {
				when {
					selectedItemIds.contains(ALL_INTERNAL_ID) -> {
						selectedItemIds = CardCondition.entries
							.filter { it.ordinal <= condition.ordinal }
							.map { it.name }
							.toSet()
						selectedItems = CardCondition.entries
							.filter { it.ordinal <= condition.ordinal }
							.toSet()
					}
					isSelected -> {
						selectedItemIds = selectedItemIds - condition.name
						selectedItems = selectedItems - condition
					}
					else -> {
						selectedItemIds = selectedItemIds + CardCondition.entries
							.filter { it.ordinal <= condition.ordinal }
							.map { it.name }
							.toSet()
						selectedItems = selectedItems + CardCondition.entries
							.filter { it.ordinal <= condition.ordinal }
							.toSet()
					}
				}
			}
		)
	}
	FloatingButton(
		text = "Select",
		show = selectedItems.isNotEmpty()
	) { onChoose(selectedItems) }
}

@Composable
private fun ConditionRow(
	colorScheme: ColorTheme.ColorSchemeParams,
	text: String,
	shortName: String,
	color: CSSColorValue,
	isSelected: Boolean,
	onClick: () -> Unit
) {
	val backgroundColor = if (isSelected) colorScheme.selectedBackground else colorScheme.unSelectedBackground
	val borderColor = if (isSelected) colorScheme.selectedBackground else Color.lightgray

	Div({
		onClick { onClick() }
		style {
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			padding(12.px)
			marginBottom(8.px)
			border(1.px, LineStyle.Solid, borderColor)
			borderRadius(12.px)
			backgroundColor(backgroundColor)
			width(98.percent)
		}
	}) {
		ConditionTag(shortName, color)

		Span({
			style {
				marginLeft(12.px)
				fontSize(16.px)
				fontWeight("500")
				color(Color.white)
			}
		}) {
			Text(text)
		}
	}
}

@Composable
private fun ConditionTag(text: String, color: CSSColorValue) {
	Div({
		style {
			width(32.px)
			height(32.px)
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			justifyContent(JustifyContent.Center)
			backgroundColor(color)
			color(Color.white)
			fontSize(14.px)
			borderRadius(4.px)
		}
	}) {
		Text(text)
	}
}

private fun CardCondition.menuOptions(): Triple<String, CSSColorValue, String> = when (this) {
	CardCondition.Mint -> Triple("Mint", Color("#7BB125"), "MI")
	CardCondition.NearMint -> Triple("Near Mint", Color("#7BB125"), "NM")
	CardCondition.SlightlyPlayed -> Triple("Slightly Played", Color("#A5B200"), "SP")
	CardCondition.ModeratelyPlayed -> Triple("Moderately Played", Color("#E39101"), "MP")
	CardCondition.Played -> Triple("Played", Color("#FD8428"), "PL")
	CardCondition.HeavilyPlayed -> Triple("Heavily Played", Color("#9C3333"), "HP")
	CardCondition.Poor -> Triple("Poor", Color("#9C3333"), "PO")
}

