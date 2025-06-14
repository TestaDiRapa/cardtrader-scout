package org.testadirapa.scryfall

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedList<T : ScryfallRootEntity>(
	@SerialName("object") val objectType: String,
	@SerialName("total_cards") val totalCards: Int,
	@SerialName("has_more") val hasMore: Boolean,
	val data: List<T>,
) {

	init {
		check(objectType == "list") {
			"Returned type is not a paginated list"
		}
	}

}