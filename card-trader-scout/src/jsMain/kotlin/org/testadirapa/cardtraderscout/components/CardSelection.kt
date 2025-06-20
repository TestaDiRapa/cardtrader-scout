package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*

import org.jetbrains.compose.web.dom.*
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.scryfall.ScryfallCard

@Composable
fun CardSelection(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: AddWatcherStateViewModel,
	cardsByOracleId: Map<String, List<ScryfallCard>>
) {
	val coroutineScope = rememberCoroutineScope()

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
			Text("Multiple Cards Found")
		}
	}

	if(!viewModel.isLoading) {
		CardSelectionMenu(
			colorScheme = colorScheme,
			baseUrl = viewModel.backendUrl,
			token = viewModel.hash,
			items = cardsByOracleId,
			labelSelector = { _, cards -> cards.first().name }
		) { selected ->
			coroutineScope.launch {
				val selectedCardList = cardsByOracleId.getValue(selected.first())
				viewModel.setChosenCard(selectedCardList)
			}
		}
	}
}
