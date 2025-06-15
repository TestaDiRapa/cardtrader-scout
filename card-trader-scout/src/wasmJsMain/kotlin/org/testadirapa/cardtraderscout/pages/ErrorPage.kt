package org.testadirapa.cardtraderscout.pages

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.testadirapa.cardtraderscout.components.BlockWithRedirectButton

@Composable
fun ErrorPage() {
	BlockWithRedirectButton(
		backgroundColor = MaterialTheme.colorScheme.errorContainer,
		title = "Error",
		text = "This page is only accessible through the @CardTraderScoutBot Telegram bot.",
		textColor = Color.Red,
	)
}
