package org.testadirapa.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpConfig {

	val json = Json {
		ignoreUnknownKeys = true
	}

	private val httpClient = HttpClient(OkHttp) {
		install(ContentNegotiation) {
			json(json)
		}
		install(HttpTimeout) {
			requestTimeoutMillis = 10_000
		}
	}

}