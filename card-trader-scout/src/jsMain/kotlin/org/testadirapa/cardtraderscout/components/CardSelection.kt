package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
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

	Title(
		colorScheme = colorScheme,
		text = "Multiple Cards Found"
	)

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
