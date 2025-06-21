package org.testadirapa.utils

import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.dto.ExtendedWatcher
import org.testadirapa.models.db.Watcher

fun enhanceWatcher(watcher: Watcher, blueprint: Blueprint): ExtendedWatcher {
	require(watcher.blueprintId == blueprint.id) {
		"Mismatching blueprint id"
	}
	return ExtendedWatcher(
		id = watcher.id,
		rev = watcher.rev,
		blueprint = blueprint,
		chatId = watcher.chatId,
		conditions = watcher.conditions,
		languages = watcher.languages,
		priceThreshold = watcher.priceThreshold,
		cardTraderZeroOnly = watcher.cardTraderZeroOnly,
		triggered = watcher.triggered
	)
}