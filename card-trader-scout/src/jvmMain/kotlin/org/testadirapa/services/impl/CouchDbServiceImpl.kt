package org.testadirapa.services.impl

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.basicAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.testadirapa.http.HttpConfig
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.models.db.BlueprintWrapper
import org.testadirapa.models.db.Keys
import org.testadirapa.models.db.PaginatedList
import org.testadirapa.models.db.PaginatedReducedList
import org.testadirapa.models.db.StoredDocument
import org.testadirapa.models.db.Watcher
import org.testadirapa.dto.NewWatcher
import org.testadirapa.dto.ExtendedWatcher
import org.testadirapa.services.CouchDbService
import org.testadirapa.utils.enhanceWatcher
import java.util.UUID

class CouchDbServiceImpl private constructor(
	couchDbUrl: String,
	username: String,
	password: String,
) : CouchDbService {

	companion object {
		private const val DATABASE_NAME = "card-trader-scout"

		private lateinit var instance: CouchDbServiceImpl
		operator fun invoke(
			couchDbUrl: String,
			username: String,
			password: String
		): CouchDbServiceImpl {
			if (!::instance.isInitialized) {
				instance = CouchDbServiceImpl(
					couchDbUrl = couchDbUrl,
					username = username,
					password = password
				)
			}
			return instance
		}

		operator fun invoke(): CouchDbServiceImpl {
			if (!::instance.isInitialized) {
				throw IllegalStateException("CouchDbServiceImpl is not initialized")
			}
			return instance
		}
	}

	private val client = HttpConfig.newHttpClient {
		defaultRequest {
			url(couchDbUrl)
			basicAuth(username, password)
			contentType(ContentType.Application.Json)
		}
		expectSuccess = false
	}

	override suspend fun createWatchers(chatId: Long, newWatcher: NewWatcher) {
		val existingWatchers = getWatchersByChatId(chatId)

		newWatcher.blueprints.forEach { blueprint ->
			val watcher = Watcher(
				id = UUID.randomUUID().toString(),
				chatId = chatId,
				blueprintId = blueprint.id,
				conditions = newWatcher.conditions,
				languages = newWatcher.languages,
				priceThreshold = (newWatcher.priceThreshold * 100).toInt(),
				cardTraderZeroOnly = newWatcher.cardTraderZeroOnly,
				triggered = false
			)
			if (watcher !in existingWatchers) {
				save(watcher)
			}
			val currentBlueprint = get<BlueprintWrapper>(blueprint.id.toString())
			if (currentBlueprint == null) {
				save(BlueprintWrapper(id = blueprint.id.toString(), blueprint = blueprint))
			} else {
				save(currentBlueprint.copy(blueprint = blueprint))
			}
		}
	}

	override suspend fun getWatchersByChatId(chatId: Long): Set<Watcher> =
		client.post {
			url {
				appendPathSegments(DATABASE_NAME, "_design", "Watcher", "_view", "by_chat_id")
				parameter("include_docs", true)
			}
			setBody(Keys(keys = listOf(chatId)))
			expectSuccess = true
		}.body<PaginatedList<Long, JsonElement, Watcher>>().rows.map { it.doc }.toSet()

	override suspend fun getExtendedWatchersByChatId(chatId: Long): List<ExtendedWatcher> {
		val watchers = getWatchersByChatId(chatId)
		val blueprintIds = watchers.map { it.blueprintId }.toSet()
		val blueprintsById = getBlueprints(blueprintIds).associateBy { it.id }
		return watchers.map {
			enhanceWatcher(
				watcher = it,
				blueprint = blueprintsById.getValue(it.blueprintId)
			)
		}.sortedBy { it.blueprint.name }
	}

	override suspend fun getWatchersByBlueprintId(blueprintId: Long): Set<Watcher> =
		client.post {
			url {
				appendPathSegments(DATABASE_NAME, "_design", "Watcher", "_view", "by_blueprint_id")
				parameter("include_docs", true)
				parameter("reduce", false)
			}
			setBody(Keys(keys = listOf(blueprintId)))
			expectSuccess = true
		}.body<PaginatedList<Long, String, Watcher>>().rows.map { it.doc }.toSet()

	override suspend fun getBlueprintIdsForAllWatchers(): Set<Long> =
		client.get {
			url {
				appendPathSegments(DATABASE_NAME, "_design", "Watcher", "_view", "by_blueprint_id")
				parameter("reduce", true)
				parameter("group_level", 1)
			}
			expectSuccess = true
		}.body<PaginatedReducedList<Long, Int>>().rows.map { it.key }.toSet()

	override suspend fun getBlueprint(blueprintId: Long): Blueprint? =
		get<BlueprintWrapper>(blueprintId.toString())?.blueprint

	private suspend fun getBlueprints(blueprintIds: Collection<Long>): List<Blueprint> =
		get<BlueprintWrapper>(blueprintIds.map { it.toString() }).map { it.blueprint }

	override suspend fun createOrUpdateWatcher(watcher: Watcher) = save(watcher)

	private suspend inline fun <reified T : StoredDocument> save(entity: T) {
		client.post {
			url {
				appendPathSegments(DATABASE_NAME)
			}
			setBody(entity)
			expectSuccess = true
		}
	}

	override suspend fun delete(docId: String, rev: String) {
		client.delete {
			url {
				appendPathSegments(DATABASE_NAME, docId)
				parameter("rev", rev)
			}
			expectSuccess = true
		}
	}

	private suspend inline fun <reified T : StoredDocument> get(id: String): T? =
		client.get {
			url {
				appendPathSegments(DATABASE_NAME, id)
			}
		}.takeIf { it.status.isSuccess() }?.body<T>()

	private suspend inline fun <reified T : StoredDocument> get(ids: List<String>): List<T> =
		client.post {
			url {
				appendPathSegments(DATABASE_NAME, "_all_docs")
				parameter("include_docs", true)
			}
			setBody(Keys(keys = ids))
			expectSuccess = true
		}.body<PaginatedList<Long, JsonElement, T>>().rows.map { it.doc }

	init {
		runBlocking {
			try {
				client.get {
					url {
						appendPathSegments(DATABASE_NAME)
					}
					expectSuccess = true
				}
			} catch (_: ClientRequestException) {
				client.put {
					url {
						appendPathSegments(DATABASE_NAME)
					}
					expectSuccess = true
				}
			}
			val blueprintRev = client.get {
				url {
					appendPathSegments(DATABASE_NAME, "_design", "Blueprint")
				}
			}.let {
				if (it.status.isSuccess()) {
					it.body<JsonObject>().getValue("_rev").jsonPrimitive.content
				} else null
			}
			client.put {
				url {
					appendPathSegments(DATABASE_NAME, "_design", "Blueprint")
				}
				expectSuccess = true
				setBody("""
					{
					  "_id": "_design/Blueprint",
					  ${blueprintRev?.let { "\"_rev\": \"$it\","} ?: ""}
					  "views": {
					    "all": {
					      "map": "function(doc) { if (doc.descriptor === 'Blueprint') emit(doc._id, doc._id)}"
					    }
					},
					  "lists": {},
					  "shows": {},
					  "updateHandlers": {},
					  "filters": {}
					}
				""".trimIndent()
				)
			}
			val watcherRev = client.get {
				url {
					appendPathSegments(DATABASE_NAME, "_design", "Watcher")
				}
			}.let {
				if (it.status.isSuccess()) {
					it.body<JsonObject>().getValue("_rev").jsonPrimitive.content
				} else null
			}
			client.put {
				url {
					appendPathSegments(DATABASE_NAME, "_design", "Watcher")
				}
				expectSuccess = true
				setBody("""
					{
					  "_id": "_design/Watcher",
					  ${watcherRev?.let { "\"_rev\": \"$it\","} ?: ""}
					  "views": {
					    "all": {
					      "map": "function(doc) { if (doc.descriptor === 'Watcher') emit(doc._id, doc._id)}"
					    },
						"by_blueprint_id": {
					      "map": "function(doc) { if (doc.descriptor === 'Watcher') emit(doc.blueprintId, doc._id)}",
						  "reduce": "_count"
					    },
						"by_chat_id": {
					      "map": "function(doc) { if (doc.descriptor === 'Watcher') emit(doc.chatId, doc._id)}"
					    }
					},
					  "lists": {},
					  "shows": {},
					  "updateHandlers": {},
					  "filters": {}
					}
				""".trimIndent()
				)
			}
		}
	}
}