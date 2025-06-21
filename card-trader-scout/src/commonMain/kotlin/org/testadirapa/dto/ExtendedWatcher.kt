package org.testadirapa.dto

import kotlinx.serialization.Serializable
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage

@Serializable
data class ExtendedWatcher(
	val id: String,
	val rev: String? = null,
	val blueprint: Blueprint,
	val chatId: Long,
	val conditions: Set<CardCondition>,
	val languages: Set<MtgLanguage>,
	val priceThreshold: Int,
	val cardTraderZeroOnly: Boolean,
	val triggered: Boolean
)