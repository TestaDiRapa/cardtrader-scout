package org.testadirapa.cardtraderscout.http

interface HttpResult<out T> {
	data class Success<T>(val data: T) : HttpResult<T>
	data class Error(val code: Int, val message: String) : HttpResult<Nothing>
}

