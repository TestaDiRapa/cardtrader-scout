package org.testadirapa.cardtraderscout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.inmo.tgbotapi.webapps.webApp
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.testadirapa.cardtraderscout.components.AlertBlock
import org.testadirapa.cardtraderscout.components.Spinner
import org.testadirapa.cardtraderscout.http.HttpResult
import org.testadirapa.cardtraderscout.http.HttpUtils.client
import org.testadirapa.cardtraderscout.http.HttpUtils.wrap
import org.testadirapa.cardtraderscout.pages.AddWatcherPage
import org.testadirapa.cardtraderscout.pages.ManageWatchersPage
import org.testadirapa.cardtraderscout.state.AddWatcherStateViewModel
import org.testadirapa.cardtraderscout.state.ManageWatchersStateViewModel
import org.testadirapa.cardtraderscout.theme.ColorTheme.getColorScheme
import org.testadirapa.cardtraderscout.utils.getBaseUrl
import org.testadirapa.cardtraderscout.utils.getQueryParams
import org.testadirapa.dto.WebAppDataWrapper

@Composable
fun App() {
	var isVerified by remember { mutableStateOf<Boolean?>(null) }
	val baseUrl = getBaseUrl()
	val queryParams = getQueryParams()
	val operation = queryParams["op"]
	val colorScheme = getColorScheme(webApp)
	LaunchedEffect("isSafe") {
		isVerified = client.post {
			url {
				takeFrom("$baseUrl/check")
			}
			contentType(ContentType.Application.Json)
			setBody(WebAppDataWrapper(webApp.initData, webApp.initDataUnsafe.hash))
		}.wrap<Boolean>().let {
			if (it is HttpResult.Success) it.data else false
		}
	}
	Div({
		style {
			width(98.percent)
			display(DisplayStyle.Flex)
			flexDirection(FlexDirection.Column)
			alignItems(AlignItems.Center)
			paddingTop(8.px)
			paddingLeft(6.px)
		}
	}) {
		when {
			isVerified == null -> Spinner()
			operation == "new" && isVerified == true -> AddWatcherPage(
				colorScheme = colorScheme,
				viewModel = AddWatcherStateViewModel(
					backendUrl = baseUrl,
					initData = webApp.initData,
					hash = webApp.initDataUnsafe.hash
				)
			)
			operation == "manage" && isVerified == true -> ManageWatchersPage(
				colorScheme = colorScheme,
				viewModel = ManageWatchersStateViewModel(
					backendUrl = baseUrl,
					initData = webApp.initData,
					hash = webApp.initDataUnsafe.hash
				)
			)
			else -> AlertBlock(
				backgroundColor = colorScheme.errorBackground,
				title = "Error",
				text = "This page is only accessible through the @CardTraderScoutBot Telegram bot.",
				textColor = colorScheme.textColor,
			)
		}
	}
}