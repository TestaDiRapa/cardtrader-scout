package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.minWidth
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.whiteSpace
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.toProxiedImageUrl

@Composable
fun BlueprintCard(
	colorScheme: ColorTheme.ColorSchemeParams,
	baseUrl: String,
	token: String,
	blueprint: Blueprint,
	isSelected: Boolean,
	onClick: () -> Unit
) {
	val backgroundColor = if (isSelected) colorScheme.selectedBackground else colorScheme.unSelectedBackground
	val borderColor = if (isSelected) colorScheme.selectedBackground else Color.lightgray

	Div({
		style {
			backgroundColor(backgroundColor)
			border(1.px, LineStyle.Solid, borderColor)
			borderRadius(24.px)
			paddingTop(8.px)
			paddingBottom(8.px)
			cursor("pointer")
			position(Position.Relative)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
			gap(4.px)
			width(100.percent)
		}
		onClick { onClick() }
	}) {

		blueprint.image?.show?.toProxiedImageUrl(baseUrl, token)?.let { imgUrl ->
			Img(src = imgUrl, alt = "Blueprint image") {
				style {
					height(128.px)
				}
			}
		}
		Div({
			style {
				width(100.percent)
				display(DisplayStyle.Flex)
				justifyContent(JustifyContent.Center)
				paddingTop(8.px)
			}
		}) {
			P({
				style {
					margin(0.px)
					display(DisplayStyle.Block)
					textAlign("Center")
					whiteSpace("normal")
					fontSize(16.px)
					fontWeight("bold")
					color(colorScheme.textColor)
					maxWidth(60.percent)
					minWidth(140.px)
				}
			}) {
				Text(blueprint.name)
			}
		}
	}
}
