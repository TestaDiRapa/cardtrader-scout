package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.utils.toProxiedImageUrl

@Composable
fun BlueprintCard(
	colorScheme: ColorScheme,
	baseUrl: String,
	token: String,
	blueprint: Blueprint,
	isSelected: Boolean,
	onClick: () -> Unit
) {
	val backgroundColor = if (isSelected) colorScheme.surfaceContainerHigh else colorScheme.tertiary
	val borderColor = if (isSelected) colorScheme.surfaceContainerHigh else Color.LightGray
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(16.dp))
			.background(backgroundColor)
			.border(1.dp, borderColor, RoundedCornerShape(16.dp))
			.clickable(onClick = onClick)
			.padding(12.dp)
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(4.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			AsyncImage(
				model = blueprint.image?.show?.toProxiedImageUrl(baseUrl, token),
				contentDescription = null,
				modifier = Modifier.size(128.dp),
				contentScale = ContentScale.Fit,
			)
			Text(
				text = blueprint.name,
				fontWeight = FontWeight.SemiBold,
				color = colorScheme.onBackground
			)
		}

		if (isSelected) {
			Icon(
				imageVector = Icons.Default.Check,
				contentDescription = "Selected",
				tint = Color.Black,
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(6.dp)
			)
		}
	}
}
