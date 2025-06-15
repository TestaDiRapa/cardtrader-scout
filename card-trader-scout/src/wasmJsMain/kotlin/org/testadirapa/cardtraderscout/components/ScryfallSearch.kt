package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScryfallSearch(
	webApp: SimpleWebApp,
	state: AddWatcherStateViewModel
) {
	var query by remember { mutableStateOf("") }
	val coroutineScope = rememberCoroutineScope()

	val onSearch: () -> Unit = {
		coroutineScope.launch {
			state.queryScryfallAndUpdateState(query)
		}
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
	) {
		OutlinedTextField(
			value = query,
			onValueChange = {
				query = it.replace(Regex("[\n\t]"), "")
				if (it.contains('\n') && it.trim().length >= 3) {
					onSearch()
				}
			},
			label = { Text("Card name or Scryfall query") },
			placeholder = { Text("Black Lotus") },
			modifier = Modifier.weight(0.9f),
		)
	}
	webApp.mainButton.apply {
		setText("Search")
		onClick {
			onSearch()
		}
		if (query.trim().length >= 3) {
			enable()
			show()
		} else {
			disable()
			hide()
		}
	}
}