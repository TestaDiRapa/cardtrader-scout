package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp

@Composable
fun ThresholdSelector(
	colorScheme: ColorScheme,
	webApp: SimpleWebApp,
	onClick: (Double, Boolean) -> Unit,
) {
	var amount by remember { mutableStateOf("") }
	var fastShipping by remember { mutableStateOf(false) }

	Box(modifier = Modifier
		.fillMaxSize()
		.padding(top = 24.dp, start = 8.dp, end = 8.dp)
	) {
		Column {
			Text(
				text = "Receive a notification if the price drops under:",
				color = colorScheme.onBackground,
			)
			Spacer(modifier = Modifier.height(8.dp))
			OutlinedTextField(
				value = amount,
				onValueChange = {
					if (it.matches(Regex("^\\d*[.,]?\\d{0,2}$"))) {
						amount = it
							.replace(",", ".")
							.replace(Regex("^0+(\\d+\\.?\\d{0,2})$"), "$1")
					}
				},
				label = { Text("Amount (€)") },
				placeholder = { Text("0.42") },
			)

			Spacer(modifier = Modifier.height(16.dp))

			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				Switch(
					checked = fastShipping,
					onCheckedChange = {
						fastShipping = it
					}
				)
				Spacer(modifier = Modifier.width(16.dp))
				Text(
					text = "Card Trader Zero Only",
					color = colorScheme.onBackground,
				)
			}
		}
		FloatingButton(
			webApp = webApp,
			text = "Confirm",
			amount.toDoubleOrNull()?.let {
				it > 0.0
			} ?: false
		) { onClick(amount.toDouble(), fastShipping) }
	}

}
