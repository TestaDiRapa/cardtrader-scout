package org.testadirapa.models.db

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.testadirapa.models.cardtrader.Blueprint
import org.testadirapa.models.db.serializers.BlueprintWrapperSerializer

@Serializable(with = BlueprintWrapperSerializer::class)
data class BlueprintWrapper(
	@SerialName("_id") override val id: String,
	@SerialName("_rev") override val rev: String? = null,
	val blueprint: Blueprint,
) : StoredDocument {
	override val descriptor = "Blueprint"
}