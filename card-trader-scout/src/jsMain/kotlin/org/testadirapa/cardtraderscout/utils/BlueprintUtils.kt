package org.testadirapa.cardtraderscout.utils

import androidx.compose.runtime.Composable
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtrader.Url
import org.testadirapa.scryfall.ScryfallCard

@Composable
fun Url.toProxiedImageUrl(baseUrl: String, token: String): String =
	"$baseUrl/ctImage${url}?token=$token"

@Composable
inline fun <reified T> T.getImageUrl(baseUrl: String, token: String): String? = when (this) {
	is Blueprint -> image?.preview?.toProxiedImageUrl(baseUrl, token)
	is ScryfallCard -> imageUris?.small
	else -> null
}