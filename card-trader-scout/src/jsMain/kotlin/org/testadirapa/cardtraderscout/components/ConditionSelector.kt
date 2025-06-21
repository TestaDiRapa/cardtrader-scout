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
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID
import org.testadirapa.cardtraderscout.utils.menuOptions

@Composable
fun ConditionSelector(
	colorScheme: ColorTheme.ColorSchemeParams,
	initialValues: Set<CardCondition> = emptySet(),
	onCancel: (() -> Unit)? = null,
	onChoose: (Set<CardCondition>) -> Unit,
) {
	var selectedItemIds by remember {
		mutableStateOf(
			if (initialValues == CardCondition.entries.toSet()) setOf(ALL_INTERNAL_ID)
			else initialValues.map { it.name }.toSet()
		)
	}
	var selectedItems by remember { mutableStateOf(initialValues) }

	Title(
		colorScheme = colorScheme,
		text = "Choose the Condition(s)"
	)

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
	if (onCancel != null) {
		FloatingSecondaryButton(
			text = "Cancel",
			show = true,
			color = colorScheme.secondaryButtonColor,
			textColor = Color("#FFFFFF"),
			onClick = onCancel
		)
	}
	FloatingMainButton(
		text = "Select",
		show = selectedItems.isNotEmpty()
	) {
		val items = selectedItems
		if (items.isNotEmpty()) {
			selectedItems = emptySet()
			selectedItemIds = emptySet()
			onChoose(items)
		}
	}
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
