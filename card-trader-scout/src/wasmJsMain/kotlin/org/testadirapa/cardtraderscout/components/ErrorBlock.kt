package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme

@Composable
fun ErrorBlock(message: String) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.background(color = MaterialTheme.colorScheme.errorContainer, shape = RoundedCornerShape(8.dp))
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		BasicText(
			text = message,
			modifier = Modifier.fillMaxWidth(),
			style = androidx.compose.ui.text.TextStyle(
				color = Color.Red,
				fontSize = 16.sp,
				textAlign = TextAlign.Center
			)
		)
	}
}
