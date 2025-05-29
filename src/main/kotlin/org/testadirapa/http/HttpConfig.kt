package org.testadirapa.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpConfig {

	private const val USER_AGENT = "CardTraderScout/1.0"

	val json = Json {
		ignoreUnknownKeys = true
	}

	fun newHttpClient(builder: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(OkHttp) {
		install(ContentNegotiation) {
			json(json)
		}
		install(HttpTimeout) {
			requestTimeoutMillis = 10_000
		}
		expectSuccess = true
		defaultRequest {
			accept(ContentType.Application.Json)
			userAgent(USER_AGENT)
		}
		builder()
	}
}