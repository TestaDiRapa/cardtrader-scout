package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp

@Composable
fun BlueprintSelector(
	colorScheme: ColorScheme,
	webApp: SimpleWebApp,
	viewModel: AddWatcherStateViewModel,
	blueprints: List<Blueprint>,
) {
	var selectedIds by remember { mutableStateOf<Set<Long>>(emptySet()) }
	Box(modifier = Modifier.fillMaxSize()) {
		LazyVerticalGrid(
			columns = GridCells.Adaptive(minSize = 160.dp),
			modifier = Modifier.fillMaxSize().padding(8.dp),
			contentPadding = PaddingValues(8.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			items(blueprints) { blueprint ->
				BlueprintCard(
					colorScheme = colorScheme,
					baseUrl = viewModel.baseUrl,
					token = viewModel.hash,
					blueprint = blueprint,
					isSelected = selectedIds.contains(blueprint.id),
					onClick = {
						selectedIds = if (selectedIds.contains(blueprint.id)) {
							selectedIds - blueprint.id
						} else {
							selectedIds + blueprint.id
						}
					}
				)
			}
		}
		Spacer(modifier = Modifier.height(42.dp))
		FloatingButton(
			webApp,
			"Select",
			selectedIds.isNotEmpty()
		) {
			viewModel.selectBlueprints(
			blueprints.filter { it.id in selectedIds }.toSet()
			)
		}
	}
}
