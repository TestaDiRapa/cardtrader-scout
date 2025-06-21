package org.testadirapa.services

import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.models.db.Watcher
import org.testadirapa.dto.NewWatcher
import org.testadirapa.dto.ExtendedWatcher
import org.testadirapa.dto.WatcherToModify

interface CouchDbService {
	suspend fun createWatchers(chatId: Long, newWatcher: NewWatcher)
	suspend fun getBlueprintIdsForAllWatchers(): Set<Long>
	suspend fun getBlueprint(blueprintId: Long): Blueprint?
	suspend fun getWatchersByChatId(chatId: Long): Set<Watcher>
	suspend fun getWatchersByBlueprintId(blueprintId: Long): Set<Watcher>
	suspend fun createOrUpdateWatcher(watcher: Watcher)
	suspend fun getExtendedWatchersByChatId(chatId: Long): List<ExtendedWatcher>
	suspend fun delete(docId: String, rev: String)
	suspend fun modifyWatcher(watcherToModify: WatcherToModify)
}