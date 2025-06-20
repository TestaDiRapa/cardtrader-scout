package org.testadirapa.cardtraderscout.pages

import androidx.compose.runtime.*
import dev.inmo.tgbotapi.webapps.webApp
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.testadirapa.cardtraderscout.components.AlertBlock
import org.testadirapa.cardtraderscout.components.BlueprintSelector
import org.testadirapa.cardtraderscout.components.CardSelection
import org.testadirapa.cardtraderscout.components.ConditionSelector
import org.testadirapa.cardtraderscout.components.FloatingButton
import org.testadirapa.cardtraderscout.components.LanguageSelector
import org.testadirapa.cardtraderscout.components.ScryfallSearch
import org.testadirapa.cardtraderscout.components.SetSelection
import org.testadirapa.cardtraderscout.components.Spinner
import org.testadirapa.cardtraderscout.components.ThresholdSelector
import org.testadirapa.cardtraderscout.state.AddWatcherState
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme

@Composable
fun AddWatcherPage(
	colorScheme: ColorTheme.ColorSchemeParams,
	viewModel: AddWatcherStateViewModel
) {
	val coroutineScope = rememberCoroutineScope()

	val state = viewModel.state

	if (state is AddWatcherState.InitialState) {
		ScryfallSearch(colorScheme, viewModel)
	}
	if (state is AddWatcherState.ScryfallEmptyResult) {
		AlertBlock(
			backgroundColor = colorScheme.warningBackground,
			text = "No card found.",
			textColor = colorScheme.warningText,
			marginTop = 8.px
		)
	}
	if (state is AddWatcherState.ScryfallError) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "Error ${state.error.code} while querying scryfall: ${state.error.message}.",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
	}
	if (state is AddWatcherState.ChooseCard) {
		CardSelection(colorScheme, viewModel, cardsByOracleId = state.cardsByOracleId)
	}

	if (state is AddWatcherState.ChooseSet) {
		SetSelection(colorScheme, viewModel, cardsBySetName = state.cardsBySet)
	}

	if (state is AddWatcherState.CardTraderEmptyResult) {
		AlertBlock(
			backgroundColor = colorScheme.warningBackground,
			text = "No CardTrader blueprint found",
			textColor = colorScheme.warningText,
			marginTop = 8.px
		)
	}

	if (state is AddWatcherState.UnknownError) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "Unknown error happened",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
	}

	if (state is AddWatcherState.CardTraderEmptyResult) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "No CardTrader blueprint found",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
	}

	if (state is AddWatcherState.CardTraderError) {
		AlertBlock(
			backgroundColor = colorScheme.errorBackground,
			text = "Error ${state.error.code} while querying card trader: ${state.error.message}",
			textColor = colorScheme.textColor,
			marginTop = 8.px
		)
	}

	if(state is AddWatcherState.Blueprints) {
		BlueprintSelector(colorScheme, viewModel, blueprints = state.bluePrints)
	}

	if (state is AddWatcherState.BlueprintsSelected) {
		ConditionSelector(colorScheme) { conditions ->
			viewModel.selectConditions(state.blueprints, conditions)
		}
	}

	if (state is AddWatcherState.ConditionSelected) {
		LanguageSelector(colorScheme) { languages ->
			viewModel.selectLanguages(state, languages)
		}
	}

	if (state is AddWatcherState.LanguageSelected) {
		ThresholdSelector(colorScheme) { price, cardTraderZeroOnly ->
			coroutineScope.launch {
				viewModel.confirmCreation(state, price, cardTraderZeroOnly)
			}
		}
	}
	if (viewModel.state is AddWatcherState.CreationError) {
		(viewModel.state as? AddWatcherState.CreationError)?.let {
			AlertBlock(
				backgroundColor = colorScheme.errorBackground,
				text = "Error ${it.error.code} while creating watcher: ${it.error.message}",
				textColor = colorScheme.textColor,
				marginTop = 8.px
			)
		}
	}
	if (viewModel.state is AddWatcherState.CreationSuccess) {
		AlertBlock(
			backgroundColor = Color("#DFF5E1"),
			title = "Success",
			text = "Creation successful, you will be notified when a listing matching your preferences will be published",
			textColor = Color("#2E7D32"),
		)
		FloatingButton(
			"Close",
			true
		) { webApp.close() }
	}

	if (viewModel.isLoading) {
		Spinner()
	}
}