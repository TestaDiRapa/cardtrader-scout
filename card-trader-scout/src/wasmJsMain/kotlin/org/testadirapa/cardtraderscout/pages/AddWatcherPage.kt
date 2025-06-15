package org.testadirapa.cardtraderscout.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.testadirapa.cardtraderscout.components.BlockWithRedirectButton
import org.testadirapa.cardtraderscout.components.BlueprintSelector
import org.testadirapa.cardtraderscout.components.CardSelection
import org.testadirapa.cardtraderscout.components.ConditionSelector
import org.testadirapa.cardtraderscout.components.ErrorBlock
import org.testadirapa.cardtraderscout.components.FloatingButton
import org.testadirapa.cardtraderscout.components.LanguageSelector
import org.testadirapa.cardtraderscout.components.ScryfallSearch
import org.testadirapa.cardtraderscout.components.SetSelection
import org.testadirapa.cardtraderscout.components.Spinner
import org.testadirapa.cardtraderscout.components.ThresholdSelector
import org.testadirapa.cardtraderscout.components.WarningBlock
import org.testadirapa.cardtraderscout.state.AddWatcherState
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp

@Composable
fun AddWatcherPage(
	colorScheme: ColorScheme,
	webApp: SimpleWebApp,
	viewModel: AddWatcherStateViewModel
) {
	val coroutineScope = rememberCoroutineScope()
	Column(
		modifier = Modifier
			.padding(start = 2.dp, end = 2.dp, top = 2.dp)
			.fillMaxHeight()
			.fillMaxWidth(),
		horizontalAlignment = Alignment.Start,
	) {
		if (viewModel.state is AddWatcherState.InitialState) {
			ScryfallSearch(webApp, viewModel)
		}
		if (viewModel.state is AddWatcherState.ScryfallEmptyResult) {
			WarningBlock("No card found")
		}
		if (viewModel.state is AddWatcherState.ScryfallError) {
			(viewModel.state as? AddWatcherState.ScryfallError)?.let {
				ErrorBlock(
					message = "Error ${it.error.code} while querying scryfall: ${it.error.message}"
				)
			}
		}
		if (viewModel.state is AddWatcherState.ChooseCard) {
			(viewModel.state as? AddWatcherState.ChooseCard)?.let {
				CardSelection(colorScheme, webApp, viewModel, cardsByOracleId = it.cardsByOracleId)
			}
		}
		if (viewModel.state is AddWatcherState.ChooseSet) {
			(viewModel.state as? AddWatcherState.ChooseSet)?.let {
				SetSelection(webApp, viewModel, cardsBySetName = it.cardsBySet)
			}
		}
		if (viewModel.state is AddWatcherState.UnknownError) {
			ErrorBlock("Unknown error happened")
		}
		if (viewModel.state is AddWatcherState.CardTraderEmptyResult) {
			WarningBlock("No CardTrader blueprint found")
		}
		if (viewModel.state is AddWatcherState.CardTraderError) {
			(viewModel.state as? AddWatcherState.CardTraderError)?.let {
				ErrorBlock(
					message = "Error ${it.error.code} while querying card trader: ${it.error.message}"
				)
			}
		}
		if (viewModel.state is AddWatcherState.Blueprints) {
			(viewModel.state as? AddWatcherState.Blueprints)?.let {
				BlueprintSelector(colorScheme, webApp, viewModel, blueprints = it.bluePrints)
			}
		}
		if (viewModel.state is AddWatcherState.BlueprintsSelected) {
			(viewModel.state as? AddWatcherState.BlueprintsSelected)?.let { state ->
				ConditionSelector(colorScheme, webApp) { viewModel.selectConditions(state.blueprints, it) }
			}
		}
		if (viewModel.state is AddWatcherState.ConditionSelected) {
			(viewModel.state as? AddWatcherState.ConditionSelected)?.let { state ->
				LanguageSelector(colorScheme, webApp) { viewModel.selectLanguages(state, it) }
			}
		}
		if (viewModel.state is AddWatcherState.LanguageSelected) {
			(viewModel.state as? AddWatcherState.LanguageSelected)?.let { state ->
				ThresholdSelector(colorScheme, webApp) { price, cardTraderZeroOnly ->
					coroutineScope.launch {
						viewModel.confirmCreation(state, price, cardTraderZeroOnly)
					}
				}
			}
		}
		if (viewModel.state is AddWatcherState.CreationError) {
			(viewModel.state as? AddWatcherState.CreationError)?.let {
				ErrorBlock(
					message = "Error ${it.error.code} while creating watcher: ${it.error.message}"
				)
			}
		}
		if (viewModel.state is AddWatcherState.CreationSuccess) {
			BlockWithRedirectButton(
				backgroundColor = Color(0xFFDFF5E1),
				title = "Success",
				text = "Creation successful, you will be notified when a listing matching your preferences will be published",
				textColor = Color(0xFF2E7D32),
			)
			FloatingButton(
				webApp,
				"Close",
				true
			) { webApp.close() }
		}
		if (viewModel.isLoading) {
			Spinner()
		}
	}
}