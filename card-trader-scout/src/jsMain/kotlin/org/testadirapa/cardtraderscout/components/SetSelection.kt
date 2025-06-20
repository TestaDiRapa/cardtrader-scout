package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*

import org.jetbrains.compose.web.dom.*
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun SetSelection(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: AddWatcherStateViewModel,
	cardsBySetName: Map<String, List<Blueprint>>
) {
	Div({
		style {
			textAlign("center")
			width(100.percent)
			marginBottom(4.px)
			paddingTop(0.px)
			marginTop(0.px)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
		}
	}) {
		H3({
			style {
				fontSize(24.px)
				color(colorScheme.textColor)
			}
		}) {
			Text("Multiple Sets Found")
		}
	}

	CardSelectionMenu(
		colorScheme = colorScheme,
		baseUrl = viewModel.backendUrl,
		token = viewModel.hash,
		items = cardsBySetName,
		includeAllOption = false,
		labelSelector = { selector, _ -> selector }
	) { chosenSets ->
		viewModel.setBlueprints(cardsBySetName.filterKeys { it in chosenSets }.values.flatten())
	}
}
