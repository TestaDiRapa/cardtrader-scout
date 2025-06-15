package org.testadirapa.cardtraderscout.utils

import androidx.compose.runtime.Composable
import org.testadirapa.cardtrader.Url

@Composable
fun Url.toProxiedImageUrl(baseUrl: String, token: String): String =
	"$baseUrl/ctImage${url}?token=$token"