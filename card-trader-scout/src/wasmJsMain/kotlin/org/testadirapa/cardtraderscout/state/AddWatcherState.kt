package org.testadirapa.cardtraderscout.state

import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.scryfall.ScryfallCard

sealed interface AddWatcherState : State {

	sealed interface InitialState : AddWatcherState

	data object UnknownError : AddWatcherState
	data object Start : InitialState
	// Scryfall states
	data class ScryfallError(val error: HttpResult.Error) : InitialState
	data object ScryfallEmptyResult : InitialState
	data class ChooseCard(val cardsByOracleId: Map<String, List<ScryfallCard>>) : AddWatcherState
	data class ChooseSet(val cardName: String, val cardsBySet: Map<String, List<Blueprint>>) : AddWatcherState
	// CardTrader states
	data class CardTraderError(val error: HttpResult.Error) : AddWatcherState
	data class Blueprints(val bluePrints: List<Blueprint>) : AddWatcherState
	data object CardTraderEmptyResult : AddWatcherState
	data class BlueprintsSelected(val blueprints: Set<Blueprint>) : AddWatcherState
	data class ConditionSelected(val blueprints: Set<Blueprint>, val conditions: Set<CardCondition>) : AddWatcherState
	data class LanguageSelected(val blueprints: Set<Blueprint>, val conditions: Set<CardCondition>, val languages: Set<MtgLanguage>) : AddWatcherState
	// Watch creation state
	data object CreationSuccess : AddWatcherState
	data class CreationError(val error: HttpResult.Error) : AddWatcherState
}