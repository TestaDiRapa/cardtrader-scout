package org.testadirapa.cardtraderscout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import org.testadirapa.cardtraderscout.components.Spinner
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.cardtraderscout.http.HttpUtils.client
import org.testadirapa.cardtraderscout.http.HttpUtils.wrap
import org.testadirapa.cardtraderscout.pages.AddWatcherPage
import org.testadirapa.cardtraderscout.pages.ErrorPage
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.telegram.webApp
import org.testadirapa.cardtraderscout.utils.getBaseUrl
import org.testadirapa.cardtraderscout.utils.getQueryParams
import org.testadirapa.cardtraderscout.utils.toColor
import org.testadirapa.dto.WebAppDataWrapper

@Composable
fun App() {
	var isVerified by remember { mutableStateOf<Boolean?>(null) }
	val queryParams = getQueryParams()
	val baseUrl = getBaseUrl()
	val operation = queryParams["op"]
	val colorScheme =
		if (webApp?.colorScheme == "dark")
			darkColorScheme(
				background = webApp?.themeParams?.backgroundColor?.toColor()
					?: Color(red = 20, green = 18, blue = 24),
				onBackground = webApp?.themeParams?.textColor?.toColor() ?: Color.White,
				tertiary = "#2A2E35".toColor(),
				surfaceContainerHigh = "#2D9CDB".toColor()
			)
		else
			lightColorScheme(
				background = webApp?.themeParams?.backgroundColor?.toColor()
					?: Color(red = 254, green = 247, blue = 255),
				onBackground = webApp?.themeParams?.textColor?.toColor() ?: Color.Black,
				tertiary = "#F1F3F4".toColor(),
				surfaceContainerHigh = "#B2DFDB".toColor()

			)
	LaunchedEffect("isSafe") {
		isVerified = webApp?.let { wa ->
			client.post {
				url {
					takeFrom("$baseUrl/check")
				}
				contentType(ContentType.Application.Json)
				setBody(WebAppDataWrapper(wa.initData, wa.initDataUnsafe.hash))
			}.wrap<Boolean>().let {
				if (it is HttpResult.Success) it.data else false
			}
		} ?: false
	}
	MaterialTheme(
		colorScheme = colorScheme,
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(colorScheme.background)
		) {
			Column {
				when {
					isVerified == null -> Spinner()
					isVerified == true && operation == "new" -> AddWatcherPage(
						colorScheme = colorScheme,
						webApp = webApp!!,
						viewModel = AddWatcherStateViewModel(
							baseUrl = baseUrl,
							initData = webApp!!.initData,
							hash = webApp!!.initDataUnsafe.hash
						)
					)
					else -> ErrorPage()
				}
			}

		}
	}
}