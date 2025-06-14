package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.scryfall.ScryfallCard

@Composable
fun CardSelection(
	viewModel: AddWatcherStateViewModel,
	cardsByOracleId: Map<String, List<ScryfallCard>>
){
	val coroutineScope = rememberCoroutineScope()
	Column {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center,
		) {
			Text(
				text = "Multiple Cards Found",
				style = MaterialTheme.typography.headlineMedium
			)
		}
		CardSelectionMenu(
			chatId = viewModel.chatId,
			token = viewModel.token,
			items = cardsByOracleId,
			includeAllOption = false,
			labelSelector = { selector, cards -> cards.first().name }
		) {
			coroutineScope.launch {
				viewModel.setChosenCard(cardsByOracleId.getValue(it.first()))
			}
		}
	}

}