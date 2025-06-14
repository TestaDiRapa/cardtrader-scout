package org.testadirapa.webview

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.utils.io.jvm.javaio.copyTo
import org.testadirapa.components.AsyncMessageQueue
import org.testadirapa.components.InteractionCache
import org.testadirapa.models.dto.NewWatcher
import org.testadirapa.services.CardTraderService
import org.testadirapa.services.CouchDbService
import org.testadirapa.services.ScryfallService
import java.io.IOException
import kotlin.concurrent.thread

class WebviewServer(
	private val cardTraderService: CardTraderService,
	private val scryfallService: ScryfallService,
	private val couchDbService: CouchDbService
) {

	private class AccessDeniedException(msg: String) : Exception(msg)

	private inline fun guarded(condition: Boolean, lazyMessage: () -> String) {
		if (!condition) {
			throw AccessDeniedException(lazyMessage())
		}
	}

	private fun RoutingContext.authenticate(): Pair<Long, String> {
		val chatId = requireNotNull(call.request.queryParameters["chatId"]?.toLongOrNull()) {
			"Invalid chatId"
		}
		val token = requireNotNull(call.request.queryParameters["token"]) {
			"Invalid token"
		}
		guarded(InteractionCache.exists(chatId, token)) {
			"Invalid interaction"
		}
		return chatId to token
	}

	private fun Application.configureRouting() {
		routing {
			get("/scryfall") {
				authenticate()
				val query = call.request.queryParameters["q"]?.takeIf { it.isNotBlank()}
				require(!query.isNullOrBlank()) { "Query is null or blank" }
				val result = scryfallService.searchCards(query)
				require(result.totalCards > 100 || result.data.map { it.oracleId }.toSet().size <= 10) {
					"Too many results, enter a more restrictive query"
				}
				call.respond(result.data)
			}
			get("/blueprints") {
				authenticate()
				val query = call.request.queryParameters["name"]?.takeIf { it.isNotBlank()}
				require(!query.isNullOrBlank()) { "Name is null or blank" }
				val blueprintsByExpansion = cardTraderService.searchBlueprintsByNameAndExpansionId(
					name = query,
					expansionId = null
				).groupBy { it.expansionId }.mapKeys { (k, _) ->
					cardTraderService.getExpansionById(k)?.name ?: "Unknown set"
				}
				call.respond(blueprintsByExpansion)
			}
			get("/ctImage/{path...}") {
				authenticate()
				val path = requireNotNull(call.parameters.getAll("path")?.joinToString("/")) {
					"Path cannot be null"
				}
				val (imageChannel, contentLength) = cardTraderService.getImage(path)
				val contentType = ContentType.Image.JPEG

				call.response.headers.append(HttpHeaders.ContentType, contentType.toString())
				if (contentLength != null) {
					call.response.headers.append(HttpHeaders.ContentLength, contentLength.toString())
				}

				call.respondOutputStream(contentType) {
					imageChannel.copyTo(this)
				}
			}
			post("/watcher") {
				val (chatId, _) = authenticate()
				val newWatcher = call.receive<NewWatcher>()
				InteractionCache.removeIfPresent(chatId)
				couchDbService.createWatchers(chatId, newWatcher)
				AsyncMessageQueue.sendMessage(chatId, "Watcher successfully created")
				call.respond(HttpStatusCode.NoContent)
			}
		}
	}

	private fun  moduleConfiguration(): Application.() -> Unit = {
		install(ContentNegotiation) {
			json()
		}
		install(CORS) {
			allowMethod(HttpMethod.Options)
			allowMethod(HttpMethod.Get)
			allowMethod(HttpMethod.Post)
			allowMethod(HttpMethod.Put)
			allowMethod(HttpMethod.Delete)
			allowMethod(HttpMethod.Patch)

			allowHeader(HttpHeaders.AccessControlAllowHeaders)
			allowHeader(HttpHeaders.ContentType)
			allowHeader(HttpHeaders.AccessControlAllowOrigin)
			allowHeader(HttpHeaders.Authorization)

			anyHost()
		}
		install(StatusPages) {
			exception<Throwable> { call, cause ->
				when (cause) {
					is AccessDeniedException -> call.respond(HttpStatusCode.Forbidden, "You are not allowed to perform this action")
					is IOException -> call.respond(HttpStatusCode.BadRequest, cause.message ?: "Something went wrong")
					is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest, cause.message ?: "Something went wrong")
					is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause.message ?: "Something went wrong")
					is ClientRequestException -> call.respond(cause.response.status, cause.message)
					is ServerResponseException -> call.respond(cause.response.status, cause.message)
					else -> call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Something went wrong")
				}
			}
		}
		configureRouting()
	}

	fun startServer() {
		thread(start = true, isDaemon = true) {
			embeddedServer(
				factory = Netty,
				port = 8081,
				host = "0.0.0.0",
				module = moduleConfiguration()
			).start(wait = true)
		}
	}

}