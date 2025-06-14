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

@Composable
fun WarningBlock(
	message: String,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(16.dp)
			.background(color = Color(0xFFFFF8D1), shape = RoundedCornerShape(8.dp)) // soft yellow background
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		BasicText(
			text = message,
			modifier = Modifier.fillMaxWidth(),
			style = androidx.compose.ui.text.TextStyle(
				color = Color(0xFF856404), // dark yellow-brown text
				fontSize = 16.sp,
				textAlign = TextAlign.Center
			)
		)
	}
}