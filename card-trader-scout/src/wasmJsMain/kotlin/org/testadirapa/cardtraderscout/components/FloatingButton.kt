package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.FloatingButton(
	text: String = "Select",
	show: Boolean,
	onClick: () -> Unit,
) {
	if (show) {
		Row(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.background(Color.White)
				.padding(16.dp),
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically
		) {
			Button(
				enabled = show,
				modifier = Modifier
					.height(42.dp)
					.defaultMinSize(minWidth = 160.dp),
				onClick = onClick
			) {
				Text(text)
			}
		}
	}
}