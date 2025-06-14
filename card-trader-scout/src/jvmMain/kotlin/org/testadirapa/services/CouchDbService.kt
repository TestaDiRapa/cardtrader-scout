package org.testadirapa.services

import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.models.db.Watcher
import org.testadirapa.dto.NewWatcher

interface CouchDbService {
	suspend fun createWatchers(chatId: Long, newWatcher: NewWatcher)
	suspend fun getBlueprintIdsForAllWatchers(): Set<Long>
	suspend fun getBlueprint(blueprintId: Long): Blueprint?
	suspend fun getWatchersByChatId(chatId: Long): Set<Watcher>
	suspend fun getWatchersByBlueprintId(blueprintId: Long): Set<Watcher>
	suspend fun createOrUpdateWatcher(watcher: Watcher)
}