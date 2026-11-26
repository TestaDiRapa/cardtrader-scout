package org.testadirapa.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import org.testadirapa.models.exceptions.UnexpectedBodyException

suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T =
	try {
		body<T>()
	} catch (e: JsonConvertException) {
		throw UnexpectedBodyException(
			message = e.message ?: "Unexpected body received",
			actualBody = bodyAsText()
		)
	}