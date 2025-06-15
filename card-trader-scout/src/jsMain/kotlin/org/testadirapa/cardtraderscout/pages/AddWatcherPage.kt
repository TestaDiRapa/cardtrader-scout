package org.testadirapa.cardtraderscout.pages

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.attributes.*
import org.testadirapa.cardtraderscout.state.AddWatcherState
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel

@Composable
fun AddWatcherPage(viewModel: AddWatcherStateViewModel) {
	val coroutineScope = rememberCoroutineScope()

	Div({
		style {
			paddingLeft(4.px)
			paddingRight(4.px)
			width(95.percent)
		}
	}) {
//		when (val state = viewModel.state) {
//			is AddWatcherState.InitialState -> {
//				ScryfallSearch(viewModel)
//			}

//			is AddWatcherState.ScryfallEmptyResult -> {
//				WarningBlock("No card found")
//			}
//
//			is AddWatcherState.ScryfallError -> {
//				ErrorBlock("Error ${state.error.code} while querying scryfall: ${state.error.message}")
//			}
//
//			is AddWatcherState.ChooseCard -> {
//				CardSelection(viewModel, cardsByOracleId = state.cardsByOracleId)
//			}
//
//			is AddWatcherState.ChooseSet -> {
//				SetSelection(viewModel, cardsBySetName = state.cardsBySet)
//			}
//
//			is AddWatcherState.UnknownError -> {
//				ErrorBlock("Unknown error happened")
//			}
//
//			is AddWatcherState.CardTraderEmptyResult -> {
//				WarningBlock("No CardTrader blueprint found")
//			}
//
//			is AddWatcherState.CardTraderError -> {
//				ErrorBlock("Error ${state.error.code} while querying card trader: ${state.error.message}")
//			}
//
//			is AddWatcherState.Blueprints -> {
//				BlueprintSelector(viewModel, blueprints = state.bluePrints)
//			}
//
//			is AddWatcherState.BlueprintsSelected -> {
//				ConditionSelector { conditions ->
//					viewModel.selectConditions(state.blueprints, conditions)
//				}
//			}
//
//			is AddWatcherState.ConditionSelected -> {
//				LanguageSelector { languages ->
//					viewModel.selectLanguages(state, languages)
//				}
//			}
//
//			is AddWatcherState.LanguageSelected -> {
//				ThresholdSelector { price, cardTraderZeroOnly ->
//					coroutineScope.launch {
//						viewModel.confirmCreation(state, price, cardTraderZeroOnly)
//					}
//				}
//			}
//
//			is AddWatcherState.CreationError -> {
//				ErrorBlock("Error ${state.error.code} while creating watcher: ${state.error.message}")
//			}
//
//			is AddWatcherState.CreationSuccess -> {
//				BlockWithRedirectButtonHtml(
//					backgroundColorHex = "#DFF5E1",
//					title = "Success",
//					text = "Creation successful, you will be notified when a listing matching your preferences will be published",
//					textColorHex = "#2E7D32"
//				)
//			}
//		}
//
//		if (viewModel.isLoading) {
//			Spinner()
//		}
	}
}