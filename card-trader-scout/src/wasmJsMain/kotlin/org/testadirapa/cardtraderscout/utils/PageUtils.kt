package org.testadirapa.cardtraderscout.utils

import kotlinx.browser.window

fun getQueryParams(): Map<String, String> {
	val queryString = window.location.search.removePrefix("?")
	return queryString
		.split("&")
		.filter { it.contains("=") }
		.associate {
			val (key, value) = it.split("=", limit = 2)
			key to value
		}
}
