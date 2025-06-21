package org.testadirapa.cardtraderscout.state

import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.dto.ExtendedWatcher

sealed interface ManageWatchersState : State {
	data object Loading : ManageWatchersState
	data class ListWatchers(val watchers: List<ExtendedWatcher>) : ManageWatchersState
	data class LoadError(val error: HttpResult.Error) : ManageWatchersState
	data object NoWatchers : ManageWatchersState
	data object UnknownError : ManageWatchersState
}