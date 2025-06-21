package org.testadirapa.cardtraderscout.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.jetbrains.compose.web.css.px
import org.testadirapa.cardtraderscout.components.AlertBlock
import org.testadirapa.cardtraderscout.components.Spinner
import org.testadirapa.cardtraderscout.components.WatcherList
import org.testadirapa.cardtraderscout.state.ManageWatchersState
import org.testadirapa.cardtraderscout.state.ManageWatchersStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun ManageWatchersPage(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: ManageWatchersStateViewModel
) {
	val state = viewModel.state

	if (state is ManageWatchersState.Loading) {
		LaunchedEffect("loadWatchers") {
			viewModel.loadWatchers()
		}
	}

	if (state is ManageWatchersState.ListWatchers) {
		WatcherList(
			colorScheme = colorScheme,
			viewModel = viewModel,
			watchers = state.watchers,
		)
	}

	if (state is ManageWatchersState.LoadError) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "Error ${state.error.code} while retrieving the watchers: ${state.error.message}.",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
	}

	if (state is ManageWatchersState.NoWatchers) {
		AlertBlock(
			backgroundColor = colorScheme.warningBackground,
			text = "No watcher found.",
			textColor = colorScheme.warningText,
			marginTop = 8.px
		)
	}

	if (viewModel.isLoading) {
		Spinner()
	}
}