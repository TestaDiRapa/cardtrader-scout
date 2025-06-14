package org.testadirapa.models.db.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.testadirapa.models.cardtrader.CardCondition
import org.testadirapa.models.cardtrader.MtgLanguage
import org.testadirapa.models.db.Watcher

object WatcherSerializer : KSerializer<Watcher> {
	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Watcher") {
		element<String>("_id")
		element<String?>("_rev")
		element<Long>("blueprintId")
		element<Long>("chatId")
		element<Set<CardCondition>>("conditions")
		element<Set<MtgLanguage>>("language")
		element<Int>("priceThreshold")
		element<Boolean>("cardTraderZeroOnly")
		element<Boolean>("triggered")
		element<String>("descriptor", isOptional = true)
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Watcher) {
		val composite = encoder.beginStructure(descriptor)
		composite.encodeStringElement(descriptor, 0, value.id)
		if (value.rev != null) {
			composite.encodeStringElement(descriptor, 1, value.rev)
		}
		composite.encodeLongElement(descriptor, 2, value.blueprintId)
		composite.encodeLongElement(descriptor, 3, value.chatId)
		composite.encodeSerializableElement(descriptor, 4, SetSerializer(CardCondition.serializer()), value.conditions)
		composite.encodeSerializableElement(descriptor, 5, SetSerializer(MtgLanguage.serializer()), value.languages)
		composite.encodeIntElement(descriptor, 6, value.priceThreshold)
		composite.encodeBooleanElement(descriptor, 7, value.cardTraderZeroOnly)
		composite.encodeBooleanElement(descriptor, 8, value.triggered)
		composite.encodeStringElement(descriptor, 9, value.descriptor)
		composite.endStructure(descriptor)
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Watcher {
		val dec = decoder.beginStructure(descriptor)
		var id: String? = null
		var rev: String? = null
		var blueprintId: Long? = null
		var chatId: Long? = null
		var conditions: Set<CardCondition> = emptySet()
		var languages: Set<MtgLanguage> = emptySet()
		var priceThreshold: Int? = null
		var cardTraderZeroOnly: Boolean? = null
		var triggered: Boolean? = null
		while (true) {
			when (dec.decodeElementIndex(descriptor)) {
				0 -> id = dec.decodeStringElement(descriptor, 0)
				1 -> rev = dec.decodeNullableSerializableElement(descriptor, 1, String.serializer())
				2 -> blueprintId = dec.decodeLongElement(descriptor, 2)
				3 -> chatId = dec.decodeLongElement(descriptor, 3)
				4 -> conditions = dec.decodeSerializableElement(descriptor, 4, SetSerializer(CardCondition.serializer()))
				5 -> languages = dec.decodeSerializableElement(descriptor, 5, SetSerializer(MtgLanguage.serializer()))
				6 -> priceThreshold = dec.decodeIntElement(descriptor, 6)
				7 -> cardTraderZeroOnly = dec.decodeBooleanElement(descriptor, 7)
				8 -> triggered = dec.decodeBooleanElement(descriptor, 8)
				9 -> {
					dec.decodeStringElement(descriptor, 4)
				}
				CompositeDecoder.DECODE_DONE -> break
			}
		}
		dec.endStructure(descriptor)

		return Watcher(
			id = id ?: error("Missing _id"),
			rev = rev,
			blueprintId = blueprintId ?: error("Missing blueprintId"),
			chatId = chatId ?: error("Missing chatId"),
			conditions = conditions,
			languages = languages,
			priceThreshold = priceThreshold ?: error("Missing priceThreshold"),
			cardTraderZeroOnly = cardTraderZeroOnly ?: error("Missing cardTraderZeroOnly"),
			triggered = triggered ?: error("Missing triggered"),
		)
	}
}
