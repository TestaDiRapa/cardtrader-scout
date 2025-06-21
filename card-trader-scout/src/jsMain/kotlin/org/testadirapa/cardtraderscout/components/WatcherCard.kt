package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtrader.toEmoji
import org.testadirapa.cardtraderscout.state.ManageWatchersStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.menuOptions
import org.testadirapa.cardtraderscout.utils.toProxiedImageUrl
import org.testadirapa.dto.ExtendedWatcher

@Composable
fun WatcherCard(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: ManageWatchersStateViewModel,
	watcher: ExtendedWatcher,
	selected: Boolean,
	onClick: () -> Unit,
) {
	Div({
		style {
			width(92.percent)
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Start)
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
				gap(4.px)
				alignItems(AlignItems.Center)
			}
		}) {
			watcher.blueprint.image?.show?.toProxiedImageUrl(
				baseUrl = viewModel.backendUrl,
				token = viewModel.hash
			)?.let { imgUrl ->
				Img(src = imgUrl, attrs = {
					style {
						width(96.px)
						borderRadius(6.px)
					}
				})
			}
		}
		Div({
			style {
				display(DisplayStyle.Flex)
				alignItems(AlignItems.Start)
				flexDirection(FlexDirection.Column)
				flexGrow(1)
				color(colorScheme.textColor)
				marginLeft(8.px)
			}
		}) {
			H3({
				style {
					marginTop(0.px)
					paddingTop(0.px)
					fontSize(14.px)
					color(colorScheme.textColor)
					marginBottom(4.px)
				}
			}) {
				Text("${watcher.blueprint.name} - ${watcher.rev}")
			}
			Div({
				style {
					display(DisplayStyle.Flex)
					color(colorScheme.textColor)
					flexDirection(FlexDirection.Row)
				}
			}) {
				Span({
					style {
						marginRight(2.px)
					}
				}) {
					Text("Conditions:")
				}
				watcher.conditions.forEach { condition ->
					val (_, color, shortName) = condition.menuOptions()
					ConditionTag(
						text = shortName,
						color = color,
						size = 16.px,
						fontSize = 7.px,
						marginRight = 2.px
					)
				}
			}
			Span({
				style {
					marginTop(4.px)
				}
			}) {
				Text("Languages: ")
				Text(watcher.languages.joinToString(" ") { it.toEmoji() },)
			}
			Span({
				style {
					marginTop(4.px)
				}
			}) {
				Text("Target price: ${watcher.priceThreshold / 100.0}")
			}
			if (watcher.cardTraderZeroOnly) {
				Span({
					style {
						marginTop(4.px)
					}
				}) {
					Text("CardTraderZero only")
				}
			}
			if (watcher.triggered) {
				Span({
					style {
						backgroundColor(Color("#DFF5E1"))
						marginTop(4.px)
						color(Color("#2E7D32"))
						borderRadius(6.px)
						fontSize(12.px)
						padding(4.px)
					}
				}) {
					Text("There is a product available at your desired price.")
				}
			}
		}
	}
}