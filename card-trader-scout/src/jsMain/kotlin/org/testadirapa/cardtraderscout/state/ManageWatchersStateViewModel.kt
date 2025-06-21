package org.testadirapa.cardtraderscout.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.cardtraderscout.http.HttpUtils.client
import org.testadirapa.cardtraderscout.http.HttpUtils.wrap
import org.testadirapa.dto.ExtendedWatcher
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

	suspend fun loadWatchers() {
		isLoading = true
		val result = client.post {
			url {
				takeFrom("$backendUrl/watcher/list")
			}
			contentType(ContentType.Application.Json)
			setBody(WebAppDataWrapper(initData, hash))
		}.wrap<List<ExtendedWatcher>>()
		isLoading = false
		state = when {
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