package org.testadirapa.dto

import kotlinx.serialization.Serializable
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage

@Serializable
data class NewWatcher(
	val blueprints: Set<Blueprint>,
	val conditions: Set<CardCondition>,
	val languages: Set<MtgLanguage>,
	val priceThreshold: Double,
	val cardTraderZeroOnly: Boolean,
	val validationData: WebAppDataWrapper
)