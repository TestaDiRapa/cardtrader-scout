package org.testadirapa.cardtraderscout.components
import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID
import org.testadirapa.cardtraderscout.utils.getImageUrl

@Composable
inline fun <reified T> SetSelectionMenu(
	baseUrl: String,
	token: String,
	items: Map<String, List<T>>,
	colorScheme: ColorTheme.ColorSchemeParams,
	crossinline labelSelector: (String, List<T>) -> String,
	crossinline onChoose: (Set<String>) -> Unit
) {
	var selectedItemIds by remember { mutableStateOf<Set<String>>(emptySet()) }
	var selectedItems by remember { mutableStateOf<Set<String>>(emptySet()) }

	Div({
		style {
			width(100.percent)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
		}
	}) {
		SetRow(
			label = "Select all",
			selected = selectedItemIds == setOf(ALL_INTERNAL_ID),
			colorScheme = colorScheme,
			onClick = {
				selectedItemIds = setOf(ALL_INTERNAL_ID)
				selectedItems = items.keys
			},
			images = emptyList()
		)

		items.forEach { (selector, cards) ->
			val isSelected = selector in selectedItemIds
			val cardUrls = cards.mapNotNull { it.getImageUrl(baseUrl, token) }

			SetRow(
				label = labelSelector(selector, cards),
				selected = isSelected,
				images = cardUrls.take(2),
				showMore = cardUrls.size >= 3,
				colorScheme = colorScheme,
				onClick = {
					when {
						selectedItemIds == setOf(ALL_INTERNAL_ID) -> {
							selectedItemIds = setOf(selector)
							selectedItems = setOf(selector)
						}
						selector in selectedItemIds -> {
							selectedItemIds = selectedItemIds - selector
							selectedItems = selectedItems - selector
						}
						else -> {
							selectedItemIds = selectedItemIds + selector
							selectedItems = selectedItems + selector
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
}

@Composable
fun SetRow(
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
