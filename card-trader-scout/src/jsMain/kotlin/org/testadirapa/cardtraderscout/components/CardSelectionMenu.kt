package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.getImageUrl

@Composable
inline fun <reified T> CardSelectionMenu(
	baseUrl: String,
	token: String,
	items: Map<String, List<T>>,
	colorScheme: ColorTheme.ColorSchemeParams,
	crossinline labelSelector: (String, List<T>) -> String,
	crossinline onChoose: (Set<String>) -> Unit
) {
	var selectedItemId by remember { mutableStateOf("") }
	var selectedItems by remember { mutableStateOf<Set<String>>(emptySet()) }

	Div({
		style {
			width(100.percent)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
		}
	}) {

		items.forEach { (selector, cards) ->
			val isSelected = selector == selectedItemId
			val cardUrls = cards.mapNotNull { it.getImageUrl(baseUrl, token) }

			CardRow(
				label = labelSelector(selector, cards),
				selected = isSelected,
				images = cardUrls.take(2),
				showMore = cardUrls.size >= 3,
				colorScheme = colorScheme,
				onClick = {
					selectedItemId = selector
					selectedItems = setOf(selector)
				}
			)
		}

		FloatingMainButton(
			text = "Select",
			show = selectedItems.isNotEmpty()
		) { onChoose(selectedItems) }
	}
}

@Composable
fun CardRow(
	label: String,
	selected: Boolean,
	images: List<String>,
	colorScheme: ColorTheme.ColorSchemeParams,
	showMore: Boolean = false,
	onClick: () -> Unit
) {
	Div({
		style {
			width(92.percent)
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			justifyContent(JustifyContent.SpaceBetween)
			padding(12.px)
			marginBottom(4.px)
			borderRadius(24.px)
			marginLeft(6.px)
			border(
				width = 1.px,
				style = LineStyle.Solid,
				color = if (selected) colorScheme.selectedBackground else Color.lightgray
			)
			backgroundColor(
				if (selected) colorScheme.selectedBackground else colorScheme.unSelectedBackground
			)
			cursor("pointer")
		}
		onClick { onClick() }
	}) {
		Div({
			style {
				display(DisplayStyle.Flex)
				alignItems(AlignItems.Center)
				flexGrow(1)
			}
		}) {
			Input(type = InputType.Radio) {
				checked(selected)
				onClick { onClick() }
			}
			Span({
				style {
					marginLeft(8.px)
					color(colorScheme.textColor)
				}
			}) {
				Text(label)
			}
		}

		Div({
			style {
				display(DisplayStyle.Flex)
				gap(4.px)
				alignItems(AlignItems.Center)
			}
		}) {
			images.forEach { url ->
				Img(src = url, attrs = {
					style {
						width(48.px)
						height(64.px)
						borderRadius(6.px)
					}
				})
			}
			if (showMore) {
				Span({
					style {
						fontSize(20.px)
						color(Color.gray)
					}
				}) {
					Text("...")
				}
			}
		}
	}
}
