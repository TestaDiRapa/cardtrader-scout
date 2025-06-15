package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp

@Composable
fun SetSelection(
	webApp: SimpleWebApp,
	viewModel: AddWatcherStateViewModel,
	cardsBySetName: Map<String, List<Blueprint>>
){
	Column {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center,
		) {
			Text(
				text = "Multiple Sets Found",
				style = MaterialTheme.typography.headlineMedium
			)
		}
		CardSelectionMenu(
			colorScheme = colorScheme,
			webApp = webApp,
			baseUrl = viewModel.baseUrl,
			token = viewModel.hash,
			items = cardsBySetName,
			includeAllOption = true,
			labelSelector = { selector, _ -> selector }
		) { chosenSets ->
			viewModel.setBlueprints(cardsBySetName.filterKeys { it in chosenSets }.values.flatten())
		}
	}

}