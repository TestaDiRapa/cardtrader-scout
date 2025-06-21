package org.testadirapa.cardtraderscout.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dev.inmo.tgbotapi.webapps.webApp
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.testadirapa.cardtraderscout.components.AlertBlock
import org.testadirapa.cardtraderscout.components.ConditionSelector
import org.testadirapa.cardtraderscout.components.ConfirmWatcherDeletion
import org.testadirapa.cardtraderscout.components.FloatingMainButton
import org.testadirapa.cardtraderscout.components.LanguageSelector
import org.testadirapa.cardtraderscout.components.Spinner
import org.testadirapa.cardtraderscout.components.ThresholdSelector
import org.testadirapa.cardtraderscout.components.WatcherList
import org.testadirapa.cardtraderscout.state.ManageWatchersState
import org.testadirapa.cardtraderscout.state.ManageWatchersStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun ManageWatchersPage(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: ManageWatchersStateViewModel
) {
	var restartCount by remember { mutableStateOf(0) }
	val coroutineScope = rememberCoroutineScope()
	val state = viewModel.state
	if (state is ManageWatchersState.Loading) {
		LaunchedEffect(restartCount) {
			viewModel.loadWatchers()
		}
	}

	if (state is ManageWatchersState.ListWatchers) {
		key(restartCount) {
			WatcherList(
				colorScheme = colorScheme,
				viewModel = viewModel,
				watchers = state.watchers,
			)
		}
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

	if (state is ManageWatchersState.ConfirmDeletion) {
		key(restartCount) {
			ConfirmWatcherDeletion(
				colorScheme = colorScheme,
				viewModel = viewModel,
				watcher = state.watcher,
			)
		}
	}

	if (state is ManageWatchersState.DeleteError) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "Error ${state.error.code} while deleting the watcher: ${state.error.message}.",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
		webApp.secondaryButton.apply {
			hide()
			onClick {}
		}
		FloatingMainButton(
			text = "Close",
			show = true
		) {
			restartCount = restartCount + 1
			viewModel.reset()
		}
	}

	if (state is ManageWatchersState.UpdateWatcherChooseCondition) {
		key(restartCount) {
			ConditionSelector(
				colorScheme = colorScheme,
				initialValues = state.watcher.conditions,
				onCancel = {
					restartCount = restartCount + 1
					viewModel.cancelOperation()
				},
				onChoose = { newCondition ->
					viewModel.editConditions(state.watcher, newCondition)
				}
			)
		}
	}

	if (state is ManageWatchersState.UpdateWatcherChooseLanguage) {
		key(restartCount) {
			LanguageSelector(
				colorScheme = colorScheme,
				initialValues = state.watcher.languages,
				onCancel = {
					restartCount = restartCount + 1
					viewModel.cancelOperation()
				},
				onChoose = { newLanguages ->
					viewModel.editLanguages(state.watcher, state.conditions, newLanguages)
				}
			)
		}
	}

	if (state is ManageWatchersState.UpdateWatcherChoosePrice) {
		key(restartCount) {
			ThresholdSelector(
				colorScheme = colorScheme,
				initialAmount = (state.watcher.priceThreshold / 100.0).toString(),
				initialFastShipping = state.watcher.cardTraderZeroOnly,
				onCancel = {
					restartCount = restartCount + 1
					viewModel.cancelOperation()
				},
				onClick = { price, zeroOnly ->
					coroutineScope.launch {
						viewModel.modifyWatcher(
							watcher = state.watcher,
							newConditions = state.conditions,
							newLanguages = state.languages,
							newPrice = price,
							cardTraderZeroOnly = zeroOnly,
						)
					}
				}
			)
		}
	}

	if (state is ManageWatchersState.ModifyError) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "Error ${state.error.code} while modifying the watcher: ${state.error.message}.",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
		webApp.secondaryButton.apply {
			hide()
			onClick {}
		}
		FloatingMainButton(
			text = "Go back",
			show = true
		) {
			restartCount = restartCount + 1
			viewModel.reset()
		}
	}

	if (viewModel.isLoading) {
		Spinner()
	}
}