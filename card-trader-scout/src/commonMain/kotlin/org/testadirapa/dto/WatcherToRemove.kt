package org.testadirapa.dto

import kotlinx.serialization.Serializable

@Serializable
class WatcherToRemove(
	val watcherId: String,
	val watcherRev: String,
	val webData: WebAppDataWrapper
)