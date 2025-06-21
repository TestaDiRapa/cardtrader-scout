package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px
import org.testadirapa.cardtraderscout.state.ManageWatchersStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.dto.ExtendedWatcher

@Composable
fun ConfirmWatcherDeletion(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: ManageWatchersStateViewModel,
	watcher: ExtendedWatcher
) {
	val coroutineScope = rememberCoroutineScope()

	Title(
		colorScheme = colorScheme,
		text = "Do you want to delete this watcher?",
	)
	WatcherCard(
		colorScheme = colorScheme,
		watcher = watcher,
		viewModel = viewModel,
		selected = false,
		onClick = {}
	)
	AlertBlock(
		backgroundColor = colorScheme.warningBackground,
		text = "This operation cannot be undone.",
		textColor = colorScheme.warningText,
		marginTop = 8.px
	)

	FloatingSecondaryButton(
		text = "Go Back",
		show = !viewModel.isLoading,
		color = colorScheme.secondaryButtonColor,
		textColor = Color("#FFFFFF"),
	) {
		viewModel.cancelOperation()
	}
	FloatingMainButton(
		text = "Delete",
		show = !viewModel.isLoading,
		color = colorScheme.redButtonColor,
	) {
		coroutineScope.launch {
			viewModel.deleteWatcher(watcher)
		}
	}
}