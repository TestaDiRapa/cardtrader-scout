package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.testadirapa.cardtraderscout.components.styles.textInputStyle
import org.testadirapa.cardtraderscout.state.ManageWatchersStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.dto.ExtendedWatcher

@Composable
fun WatcherList(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: ManageWatchersStateViewModel,
	watchers: List<ExtendedWatcher>
) {
	var selectedItem by remember { mutableStateOf<String?>(null) }
	var filter by remember { mutableStateOf("") }

	Div({
		style {
			width(100.percent)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
		}
	}) {
		Input(type = InputType.Text) {
			value(filter)
			onInput {
				filter = it.value.replace(Regex("[\n\t]"), "")
			}
			placeholder("Filter by name")
			textInputStyle(colorScheme) {
				marginBottom(8.px)
			}
		}
		watchers.filter {
			filter.isBlank() || Regex(".*${filter.formatForSearch()}.*").matches(it.blueprint.name.formatForSearch())
		}.forEach {
			WatcherCard(
				colorScheme = colorScheme,
				watcher = it,
				viewModel = viewModel,
				selected = selectedItem == it.id,
				onClick = {
					selectedItem = if (selectedItem != it.id) {
						it.id
					} else {
						null
					}
				}
			)
		}
	}
	FloatingSecondaryButton(
		text = "Delete",
		show = selectedItem != null,
		color = colorScheme.redButtonColor,
		textColor = Color("#FFFFFF")
	) {
		val itemId = selectedItem
		if (itemId != null) {
			selectedItem = null
			viewModel.chooseWatcherToDelete(watchers.first { it.id == itemId })
		}
	}
	FloatingMainButton(
		text = "Edit",
		show = selectedItem != null,
	) {
		val itemId = selectedItem
		if (itemId != null) {
			selectedItem = null
			viewModel.startEdit(watchers.first { it.id == itemId })
		}
	}
}

private fun String.formatForSearch() = lowercase().replace(Regex("[^a-z0-9]"), "")