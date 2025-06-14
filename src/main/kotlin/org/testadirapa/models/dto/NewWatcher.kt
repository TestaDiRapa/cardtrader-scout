package org.testadirapa.models.dto

import kotlinx.serialization.Serializable
import org.testadirapa.models.cardtrader.Blueprint
import org.testadirapa.models.cardtrader.CardCondition
import org.testadirapa.models.cardtrader.MtgLanguage

@Serializable
data class NewWatcher(
	val blueprints: Set<Blueprint>,
	val conditions: Set<CardCondition>,
	val languages: Set<MtgLanguage>,
	val priceThreshold: Double,
	val cardTraderZeroOnly: Boolean
)