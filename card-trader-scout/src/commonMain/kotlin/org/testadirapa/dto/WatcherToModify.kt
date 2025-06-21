package org.testadirapa.dto

import kotlinx.serialization.Serializable
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage

@Serializable
data class WatcherToModify(
	val watcherId: String,
	val watcherRev: String,
	val conditions: Set<CardCondition>,
	val languages: Set<MtgLanguage>,
	val priceThreshold: Double,
	val cardTraderZeroOnly: Boolean,
	val validationData: WebAppDataWrapper
)