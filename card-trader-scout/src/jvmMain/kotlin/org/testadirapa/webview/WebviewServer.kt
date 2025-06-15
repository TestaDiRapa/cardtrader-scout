package org.testadirapa.webview

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.inmo.tgbotapi.utils.TelegramAPIUrlsKeeper
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
import io.ktor.server.http.content.staticFiles
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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.testadirapa.components.AsyncMessageQueue
import org.testadirapa.dto.NewWatcher
import org.testadirapa.dto.WebAppDataWrapper
import org.testadirapa.services.CardTraderService
import org.testadirapa.services.CouchDbService
import org.testadirapa.services.ScryfallService
import java.io.File
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class WebviewServer(
	private val cardTraderService: CardTraderService,
	private val scryfallService: ScryfallService,
	private val couchDbService: CouchDbService,
	private val urlKeeper: TelegramAPIUrlsKeeper
) {

	private val verifiedHash: Cache<String, Boolean> = Caffeine.newBuilder()
		.expireAfterWrite(10, TimeUnit.MINUTES)
		.build()

	private class AccessDeniedException(msg: String) : Exception(msg)

	private inline fun guarded(condition: Boolean, lazyMessage: () -> String) {
		if (!condition) {
			throw AccessDeniedException(lazyMessage())
		}
	}

	private fun RoutingContext.authenticate() {
		val token = requireNotNull(call.request.queryParameters["token"]) {
			"Invalid token"
		}
		guarded(verifiedHash.getIfPresent(token) == true) {
			"Invalid interaction"
		}
	}

	private val staticFolder = File("/Users/vincenzoclaudiopierro/Documents/GitHub/cardtrader-scout/card-trader-scout/build/dist/wasmJs/productionExecutable")

	private fun Application.configureRouting() {
		routing {
			staticFiles("", staticFolder) {
				default("${staticFolder.absolutePath}${File.separator}index.html")
			}
			post("/check") {
				val webAppCheckData = call.receive<WebAppDataWrapper>()
				val isSafe = urlKeeper.checkWebAppData(webAppCheckData.data, webAppCheckData.hash)
				verifiedHash.put(webAppCheckData.hash, isSafe)
				call.respond(HttpStatusCode.OK, isSafe)
			}
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
				val newWatcher = call.receive<NewWatcher>()
				guarded(
					urlKeeper.checkWebAppData(newWatcher.validationData.data, newWatcher.validationData.hash)
				) {
					"Invalid interaction"
				}
				verifiedHash.invalidate(newWatcher.validationData.hash)
				val chatId = newWatcher.validationData.extractUser().id
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

	private fun WebAppDataWrapper.extractUser(): User =
		data.split("&").firstNotNullOf { param ->
			val (key, value) = param.split("=", limit = 2)
			if (key == "user") {
				URLDecoder.decode(value, StandardCharsets.UTF_8.name())
			} else null
		}.let {
			Json.decodeFromString(it)
		}

	@Serializable
	private data class User(
		val id: Long,
		@SerialName("first_name") val firstName: String? = null,
		@SerialName("last_name") val lastName: String? = null,
		val username: String? = null,
		@SerialName("language_code") val languageCode: String? = null,
		@SerialName("allows_write_to_pm") val allowsWriteToPm: Boolean = false,
		@SerialName("photo_url") val photoUrl: String? = null,
	)

}