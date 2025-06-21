package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.testadirapa.cardtraderscout.components.styles.textInputStyle
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun ScryfallSearch(
	colorScheme: ColorTheme.ColorSchemeParams,
	state: AddWatcherStateViewModel
) {
	var query by remember { mutableStateOf("") }
	val coroutineScope = rememberCoroutineScope()

	val onSearch: () -> Unit = {
		coroutineScope.launch {
			state.queryScryfallAndUpdateState(query)
		}
	}

	Div({
		style {
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			width(100.percent)
		}
	}) {
		Input(type = InputType.Text) {
			value(query)
			onInput {
				query = it.value.replace(Regex("[\n\t]"), "")
			}
			placeholder("Card name or Scryfall query")
			textInputStyle(colorScheme)
		}

		FloatingMainButton(
			text = "Search",
			show = query.trim().length >= 3
		) { onSearch() }
	}
}
