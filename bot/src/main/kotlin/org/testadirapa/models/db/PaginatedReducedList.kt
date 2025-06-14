package org.testadirapa.models.db

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedReducedList<K, V>(
	val rows: List<ReducedRow<K, V>>
)

@Serializable
data class ReducedRow<K, V>(
	val key: K,
	val value: V,
)