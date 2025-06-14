package org.testadirapa.components

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.toChatId
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object InteractionCache {

	private val cache: Cache<IdChatIdentifier, String> = Caffeine.newBuilder()
		.expireAfterWrite(10, TimeUnit.MINUTES)
		.build()

	fun addInteraction(chatId: IdChatIdentifier, token: String) {
		cache.put(chatId, token)
	}

	fun exists(chatId: Long, token: String): Boolean =
		cache.getIfPresent(chatId.toChatId()) == token

	fun removeIfPresent(chatId: Long) {
		cache.invalidate(chatId.toChatId())
	}

}