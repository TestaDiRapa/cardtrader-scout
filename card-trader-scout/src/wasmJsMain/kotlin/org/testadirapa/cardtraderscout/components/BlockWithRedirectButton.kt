package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.browser.window

@Composable
fun BlockWithRedirectButton(
	backgroundColor: Color,
	title: String,
	text: String,
	textColor: Color,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = title,
				fontWeight = FontWeight.Bold,
				color = textColor
			)
			Spacer(modifier = Modifier.height(16.dp))
			BasicText(
				text = text,
				modifier = Modifier.fillMaxWidth(),
				style = TextStyle(
					color = textColor,
					fontSize = 16.sp
				)
			)
			Spacer(modifier = Modifier.height(16.dp))
			Button(onClick = {
//				window.open("https://t.me/CardTraderScoutBot", "_blank")
			}) {
				Text("CardTraderScoutBot")
			}
		}
	}
}