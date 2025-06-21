package org.testadirapa.cardtraderscout.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.cardtraderscout.http.HttpUtils.client
import org.testadirapa.cardtraderscout.http.HttpUtils.wrap
import org.testadirapa.cardtraderscout.http.HttpUtils.wrapNoContent
import org.testadirapa.dto.ExtendedWatcher
import org.testadirapa.dto.WatcherToModify
import org.testadirapa.dto.WatcherToRemove
import org.testadirapa.dto.WebAppDataWrapper

class ManageWatchersStateViewModel(
	val backendUrl: String,
	val initData: String,
	val hash: String
) {
	var state by mutableStateOf<ManageWatchersState>(ManageWatchersState.Loading)
		private set
	var isLoading by mutableStateOf(false)
		private set
	var watchers by mutableStateOf<List<ExtendedWatcher>>(emptyList())
		private set

	fun reset() {
		state = ManageWatchersState.Loading
	}

	suspend fun loadWatchers() {
		state = loadWatchersAndGetState()
	}

	fun chooseWatcherToDelete(watcher: ExtendedWatcher) {
		state = ManageWatchersState.ConfirmDeletion(watcher)
	}

	fun cancelOperation() {
		state = ManageWatchersState.ListWatchers(watchers)
	}

	fun startEdit(watcher: ExtendedWatcher) {
		state = ManageWatchersState.UpdateWatcherChooseCondition(watcher)
	}

	fun editConditions(watcher: ExtendedWatcher, newConditions: Set<CardCondition>) {
		state = ManageWatchersState.UpdateWatcherChooseLanguage(watcher, newConditions)
	}

	fun editLanguages(watcher: ExtendedWatcher, newConditions: Set<CardCondition>, languages: Set<MtgLanguage>) {
		state = ManageWatchersState.UpdateWatcherChoosePrice(watcher, newConditions, languages)
	}

	suspend fun modifyWatcher(
		watcher: ExtendedWatcher,
		newConditions: Set<CardCondition>,
		newLanguages: Set<MtgLanguage>,
		newPrice: Double,
		cardTraderZeroOnly: Boolean
	) {
		isLoading = true
		val result = client.put {
			url {
				takeFrom("$backendUrl/watcher")
				parameter("token", hash)
			}
			contentType(ContentType.Application.Json)
			setBody(
				WatcherToModify(
					watcherId = watcher.id,
					watcherRev = watcher.rev ?: "",
					conditions = newConditions,
					languages = newLanguages,
					priceThreshold = newPrice,
					cardTraderZeroOnly = cardTraderZeroOnly,
					validationData = WebAppDataWrapper(initData, hash)
				)
			)
		}.wrapNoContent()
		isLoading = false
		state = when (result) {
			is HttpResult.Error -> ManageWatchersState.ModifyError(result)
			is HttpResult.Success -> loadWatchersAndGetState()
		}
	}

	suspend fun deleteWatcher(watcher: ExtendedWatcher) {
		isLoading = true
		val result = client.post {
			url {
				takeFrom("$backendUrl/watcher/purge")
			}
			contentType(ContentType.Application.Json)
			setBody(
				WatcherToRemove(
					watcherId = watcher.id,
					watcherRev = watcher.rev ?: "",
					webData = WebAppDataWrapper(initData, hash)
				)
			)
		}.wrapNoContent()
		isLoading = false
		state = when (result) {
			is HttpResult.Error -> ManageWatchersState.DeleteError(result)
			is HttpResult.Success -> loadWatchersAndGetState()
		}
	}

	private suspend fun loadWatchersAndGetState(): ManageWatchersState {
		isLoading = true
		val result = client.post {
			url {
				takeFrom("$backendUrl/watcher/list")
			}
			contentType(ContentType.Application.Json)
			setBody(WebAppDataWrapper(initData, hash))
		}.wrap<List<ExtendedWatcher>>()
		isLoading = false
		return when {
			result is HttpResult.Error -> ManageWatchersState.LoadError(result)
			result is HttpResult.Success && result.data.isEmpty() -> ManageWatchersState.NoWatchers
			result is HttpResult.Success -> {
				watchers = result.data
				ManageWatchersState.ListWatchers(result.data)
			}
			else -> ManageWatchersState.UnknownError
		}
	}
}