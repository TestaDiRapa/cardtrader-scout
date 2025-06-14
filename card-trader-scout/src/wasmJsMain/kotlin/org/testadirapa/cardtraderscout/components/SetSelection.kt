package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel

@Composable
fun SetSelection(
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
			chatId = viewModel.chatId,
			token = viewModel.token,
			items = cardsBySetName,
			includeAllOption = true,
			labelSelector = { selector, _ -> selector }
		) { chosenSets ->
			viewModel.setBlueprints(cardsBySetName.filterKeys { it in chosenSets }.values.flatten())
		}
	}

}