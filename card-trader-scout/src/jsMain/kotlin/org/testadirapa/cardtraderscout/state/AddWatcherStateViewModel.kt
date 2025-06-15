package org.testadirapa.cardtraderscout.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.cardtraderscout.http.HttpUtils.client
import org.testadirapa.cardtraderscout.http.HttpUtils.wrap
import org.testadirapa.cardtraderscout.http.HttpUtils.wrapNoContent
import org.testadirapa.dto.NewWatcher
import org.testadirapa.scryfall.ScryfallCard

class AddWatcherStateViewModel(
	val backendUrl: String,
	val initData: String,
	val hash: String
) {
	var state by mutableStateOf<AddWatcherState>(AddWatcherState.Start)
		private set
	var isLoading by mutableStateOf(false)
		private set

	suspend fun queryScryfallAndUpdateState(query: String) {
		isLoading = true
		val result = client.get {
			url {
				takeFrom("$backendUrl/scryfall")
				parameter("q", query)
				parameter("token", hash)
			}
		}.wrap<List<ScryfallCard>>()
		isLoading = false
		state = when {
			result is HttpResult.Error && result.code == 404 -> AddWatcherState.ScryfallEmptyResult
			result is HttpResult.Error-> AddWatcherState.ScryfallError(result)
			result is HttpResult.Success && result.data.isEmpty() -> AddWatcherState.ScryfallEmptyResult
			result is HttpResult.Success -> {
				val cardsByOracleId = result.data.groupBy { it.oracleId ?: "unknown" }
				if (cardsByOracleId.size == 1) {
					val cardName = cardsByOracleId.values.first().first().name
					retrieveBlueprintsAndGetState(cardName)
				} else {
					AddWatcherState.ChooseCard(cardsByOracleId)
				}
			}
			else -> AddWatcherState.UnknownError
		}
	}

	suspend fun setChosenCard(cards: List<ScryfallCard>) {
		val cardName = cards.first().name
		state = retrieveBlueprintsAndGetState(cardName)
	}

	fun setBlueprints(blueprints: List<Blueprint>) {
		state = AddWatcherState.Blueprints(blueprints)
	}

	fun selectBlueprints(blueprints: Set<Blueprint>) {
		state = AddWatcherState.BlueprintsSelected(blueprints)
	}

	fun selectConditions(blueprints: Set<Blueprint>, conditions: Set<CardCondition>) {
		state = AddWatcherState.ConditionSelected(blueprints, conditions)
	}

	fun selectLanguages(currentState: AddWatcherState.ConditionSelected, languages: Set<MtgLanguage>) {
		state = AddWatcherState.LanguageSelected(currentState.blueprints, currentState.conditions, languages)
	}

	suspend fun confirmCreation(currentSate: AddWatcherState.LanguageSelected, price: Double, cardTraderZeroOnly: Boolean) {
		isLoading = true
		val result = client.post {
			url {
				takeFrom("$backendUrl/watcher")
				parameter("token", hash)
			}
			contentType(ContentType.Application.Json)
			setBody(
				NewWatcher(
					blueprints = currentSate.blueprints,
					conditions = currentSate.conditions,
					languages = currentSate.languages,
					priceThreshold = price,
					cardTraderZeroOnly = cardTraderZeroOnly,
				)
			)
		}.wrapNoContent()
		isLoading = false
		state = if (result is HttpResult.Error) {
			AddWatcherState.CreationError(result)
		} else AddWatcherState.CreationSuccess
	}

	private suspend fun retrieveBlueprintsAndGetState(cardName: String): AddWatcherState {
		isLoading = true
		val result = client.get {
			url {
				takeFrom("$backendUrl/blueprints")
				parameter("name", cardName)
				parameter("token", hash)
			}
		}.wrap<Map<String, List<Blueprint>>>()
		isLoading = false
		return when {
			result is HttpResult.Error -> AddWatcherState.CardTraderError(result)
			result is HttpResult.Success && result.data.isEmpty() -> AddWatcherState.CardTraderEmptyResult
			result is HttpResult.Success && result.data.size == 1 ->
				AddWatcherState.Blueprints(result.data.values.first())
			result is HttpResult.Success -> AddWatcherState.ChooseSet(cardName, result.data)
			else -> AddWatcherState.UnknownError
		}
	}

}