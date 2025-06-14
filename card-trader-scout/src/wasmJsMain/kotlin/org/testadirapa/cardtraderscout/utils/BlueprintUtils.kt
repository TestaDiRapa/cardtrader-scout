package org.testadirapa.cardtraderscout.utils

import androidx.compose.runtime.Composable
import org.testadirapa.cardtrader.Url

import org.testadirapa.cardtraderscout.utils.Strings.backendUrl

@Composable
fun Url.toProxiedImageUrl(chatId: Long, token: String): String =
	"$backendUrl/ctImage${url}?chatId=$chatId&token=$token"