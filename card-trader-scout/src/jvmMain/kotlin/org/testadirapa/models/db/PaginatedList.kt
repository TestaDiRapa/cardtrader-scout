package org.testadirapa.models.db

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedList<K, V, D>(
	@SerialName("total_rows") val totalRows: Int,
	val offset: Int?,
	val rows: List<Row<K, V, D>>
)

@Serializable
data class Row<K, V, D>(
	val id: String,
	val key: K,
	val value: V,
	val doc: D
)