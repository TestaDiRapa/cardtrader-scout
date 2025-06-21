package org.testadirapa.cardtraderscout.state

import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.dto.ExtendedWatcher

sealed interface ManageWatchersState : State {
	data object Loading : ManageWatchersState
	data class ListWatchers(val watchers: List<ExtendedWatcher>) : ManageWatchersState
	data class LoadError(val error: HttpResult.Error) : ManageWatchersState
	data object NoWatchers : ManageWatchersState
	data object UnknownError : ManageWatchersState
	data class ConfirmDeletion(val watcher: ExtendedWatcher) : ManageWatchersState
	data class DeleteError(val error: HttpResult.Error) : ManageWatchersState
	data class UpdateWatcherChooseCondition(val watcher: ExtendedWatcher) : ManageWatchersState
	data class UpdateWatcherChooseLanguage(val watcher: ExtendedWatcher, val conditions: Set<CardCondition>) : ManageWatchersState
	data class UpdateWatcherChoosePrice(
		val watcher: ExtendedWatcher,
		val conditions: Set<CardCondition>,
		val languages: Set<MtgLanguage>,
	) : ManageWatchersState
	data class ModifyError(val error: HttpResult.Error) : ManageWatchersState
}