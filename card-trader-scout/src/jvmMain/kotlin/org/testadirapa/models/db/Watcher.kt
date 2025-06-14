package org.testadirapa.models.db

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.models.db.serializers.WatcherSerializer

@Serializable(with = WatcherSerializer::class)
data class Watcher(
	@SerialName("_id") override val id: String,
	@SerialName("_rev") override val rev: String? = null,
	val blueprintId: Long,
	val chatId: Long,
	val conditions: Set<CardCondition>,
	val languages: Set<MtgLanguage>,
	val priceThreshold: Int,
	val cardTraderZeroOnly: Boolean,
	val triggered: Boolean
) : StoredDocument{

	override val descriptor = "Watcher"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Watcher
		if (blueprintId != other.blueprintId) return false
		if (chatId != other.chatId) return false
		if (priceThreshold != other.priceThreshold) return false
		if (cardTraderZeroOnly != other.cardTraderZeroOnly) return false
		if (conditions != other.conditions) return false
		if (languages != other.languages) return false

		return true
	}

	override fun hashCode(): Int {
		var result = priceThreshold.hashCode()
		result = 31 * result + blueprintId.hashCode()
		result = 31 * result + chatId.hashCode()
		result = 31 * result + cardTraderZeroOnly.hashCode()
		result = 31 * result + conditions.hashCode()
		result = 31 * result + languages.hashCode()
		return result
	}
}