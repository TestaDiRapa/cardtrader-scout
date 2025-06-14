package org.testadirapa.models.db.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.testadirapa.models.cardtrader.Blueprint
import org.testadirapa.models.db.BlueprintWrapper

object BlueprintWrapperSerializer : KSerializer<BlueprintWrapper> {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BlueprintWrapper") {
		element<String>("_id")
		element<String?>("_rev")
		element("blueprint", Blueprint.serializer().descriptor)
		element<String>("descriptor", isOptional = true)
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: BlueprintWrapper) {
		val composite = encoder.beginStructure(descriptor)
		composite.encodeStringElement(descriptor, 0, value.id)
		if (value.rev != null) {
			composite.encodeStringElement(descriptor, 1, value.rev)
		}
		composite.encodeSerializableElement(descriptor, 2, Blueprint.serializer(), value.blueprint)
		composite.encodeStringElement(descriptor, 3, value.descriptor)
		composite.endStructure(descriptor)
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun deserialize(decoder: Decoder): BlueprintWrapper {
		val dec = decoder.beginStructure(descriptor)
		var id: String? = null
		var rev: String? = null
		var blueprint: Blueprint? = null
		while (true) {
			when (dec.decodeElementIndex(descriptor)) {
				0 -> id = dec.decodeStringElement(descriptor, 0)
				1 -> rev = dec.decodeNullableSerializableElement(descriptor, 1, String.serializer())
				2 -> blueprint = dec.decodeSerializableElement(descriptor, 2, Blueprint.serializer())
				3 -> {
					dec.decodeStringElement(descriptor, 4)
				}
				CompositeDecoder.DECODE_DONE -> break
			}
		}
		dec.endStructure(descriptor)

		return BlueprintWrapper(
			id = id ?: error("Missing _id"),
			rev = rev,
			blueprint = blueprint ?: error("Missing blueprint"),
		)
	}
}
