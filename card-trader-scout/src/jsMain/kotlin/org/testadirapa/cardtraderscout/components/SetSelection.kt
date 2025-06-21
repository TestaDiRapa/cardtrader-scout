package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun SetSelection(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: AddWatcherStateViewModel,
	cardsBySetName: Map<String, List<Blueprint>>
) {
	Title(
		colorScheme = colorScheme,
		text = "Multiple Sets Found"
	)

	SetSelectionMenu(
		colorScheme = colorScheme,
		baseUrl = viewModel.backendUrl,
		token = viewModel.hash,
		items = cardsBySetName,
		labelSelector = { selector, _ -> selector }
	) { chosenSets ->
		viewModel.setBlueprints(cardsBySetName.filterKeys { it in chosenSets }.values.flatten())
	}
}
