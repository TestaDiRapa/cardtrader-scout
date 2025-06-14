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
		text = "Operation not allowed, please go on the telegram bot to use its functionalities.",
		textColor = Color.Red,
	)
}
