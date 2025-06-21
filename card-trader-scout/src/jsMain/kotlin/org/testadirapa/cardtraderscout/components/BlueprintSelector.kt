package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.gridTemplateColumns
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun BlueprintSelector(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: AddWatcherStateViewModel,
	blueprints: List<Blueprint>
) {
	var selectedIds by remember { mutableStateOf<Set<Long>>(emptySet()) }

	Div({
		style {
			display(DisplayStyle.Grid)
			gridTemplateColumns("repeat(2, 1fr)")
			gap(8.px)
		}
	}) {
		blueprints.forEach { blueprint ->
			BlueprintCard(
				colorScheme = colorScheme,
				baseUrl = viewModel.backendUrl,
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
	FloatingMainButton(
		"Select",
		selectedIds.isNotEmpty()
	) {
		viewModel.selectBlueprints(
			blueprints.filter { it.id in selectedIds }.toSet()
		)
	}
}