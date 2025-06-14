package org.testadirapa.cardtraderscout

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.cardtraderscout.http.HttpUtils.client
import org.testadirapa.cardtraderscout.http.HttpUtils.wrap
import org.testadirapa.cardtraderscout.pages.AddWatcherPage
import org.testadirapa.cardtraderscout.pages.ErrorPage
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.telegram.webApp
import org.testadirapa.cardtraderscout.utils.Strings.backendUrl
import org.testadirapa.cardtraderscout.utils.getQueryParams
import org.testadirapa.dto.WebAppDataWrapper

@Composable
fun App() {
	var isVerified by remember { mutableStateOf(false) }
	val queryParams = getQueryParams()
	val operation = queryParams["op"]
	MaterialTheme {
		LaunchedEffect("isSafe") {
			isVerified = webApp?.let { wa ->
				client.post {
					url {
						takeFrom("$backendUrl/check")
					}
					contentType(ContentType.Application.Json)
					setBody(WebAppDataWrapper(wa.initData, wa.initDataUnsafe.hash))
				}.wrap<Boolean>().let {
					if (it is HttpResult.Success) it.data else false
				}
			} ?: false
		}
		when {
			operation == null || webApp == null || !isVerified -> ErrorPage()
			operation == "new" -> AddWatcherPage(AddWatcherStateViewModel(123, ""))
			else -> ErrorPage()
		}
	}
}