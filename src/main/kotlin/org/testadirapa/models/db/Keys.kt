package org.testadirapa.models.db

import kotlinx.serialization.Serializable

@Serializable
data class Keys<T>(
	val keys: List<T>
)