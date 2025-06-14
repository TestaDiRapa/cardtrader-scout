package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScryfallSearch(state: AddWatcherStateViewModel) {
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
			modifier = Modifier.weight(1f),
		)
		Spacer(modifier = Modifier.width(8.dp))
		Box(modifier = Modifier.padding(top = 10.dp)) {
			IconButton(
				enabled = query.trim().length >= 3,
				onClick = onSearch,
				modifier = Modifier
					.size(52.dp)
					.clip(CircleShape)
					.background(
						if (query.trim().length >= 3) MaterialTheme.colorScheme.primaryContainer
						else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
					)
			) {
				Icon(
					imageVector = Icons.Default.Search,
					contentDescription = "Search",
					tint =
						if (query.trim().length >= 3) MaterialTheme.colorScheme.primary
						else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
					modifier = Modifier.size(24.dp)
				)
			}
		}
	}
}