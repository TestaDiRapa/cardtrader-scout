package org.testadirapa.cardtraderscout.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.testadirapa.cardtraderscout.utils.Void

object HttpUtils {

	val json = Json {
		isLenient = true
	}

	val client = HttpClient(Js) {
		install(ContentNegotiation) {
			 json(json)
		}
		install(HttpTimeout) {
			requestTimeoutMillis = 10_000
		}
	}

	suspend inline fun <reified T> HttpResponse.wrap(): HttpResult<T> =
		if (status.isSuccess()) {
			HttpResult.Success(body<T>())
		} else {
			HttpResult.Error(code = status.value, message = bodyAsText())
		}

	suspend inline fun HttpResponse.wrapNoContent(): HttpResult<Void> =
		if (status.isSuccess()) {
			HttpResult.Success(Void)
		} else {
			HttpResult.Error(code = status.value, message = bodyAsText())
		}

}