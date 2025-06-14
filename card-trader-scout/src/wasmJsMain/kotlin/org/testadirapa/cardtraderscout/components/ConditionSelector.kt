package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.testadirapa.cardtrader.CardCondition
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID

@Composable
fun ConditionSelector(
	onChoose: (Set<CardCondition>) -> Unit,
) {
	var selectedItemIds by remember { mutableStateOf<Set<String>>(emptySet()) }
	var selectedItems by remember { mutableStateOf<Set<CardCondition>>(emptySet()) }

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(bottom = 72.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center,
			) {
				Text(
					text = "Choose the condition(s)",
					style = MaterialTheme.typography.headlineMedium
				)
			}

			val isSelected = selectedItemIds.contains(ALL_INTERNAL_ID)
			val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White
			val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 4.dp)
					.clip(RoundedCornerShape(16.dp))
					.border(1.dp, borderColor, RoundedCornerShape(16.dp))
					.background(backgroundColor)
					.clickable {
						selectedItemIds = setOf(ALL_INTERNAL_ID)
						selectedItems = CardCondition.entries.toSet()
					}
					.padding(12.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				ConditionTag("NA", Color.LightGray)

				Spacer(modifier = Modifier.width(8.dp))

				Text(
					"Any condition",
					style = MaterialTheme.typography.bodyLarge,
					modifier = Modifier.weight(1f)
				)
			}

			CardCondition.entries.forEach { condition ->
				val (name, color, shortName) = condition.menuOptions()
				val isSelected = selectedItemIds.contains(condition.name)
				val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White
				val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 4.dp)
						.clip(RoundedCornerShape(16.dp))
						.border(1.dp, borderColor, RoundedCornerShape(16.dp))
						.background(backgroundColor)
						.clickable {
							when {
								selectedItemIds.contains(ALL_INTERNAL_ID) -> {
									selectedItemIds = CardCondition.entries
										.filter { it.ordinal <= condition.ordinal }
										.map { it.name }
										.toSet()
									selectedItems = setOf(condition)
								}
								selectedItemIds.contains(condition.name) -> {
									selectedItemIds = selectedItemIds - condition.name
									selectedItems = selectedItems - condition
								}
								else -> {
									selectedItemIds = selectedItemIds + CardCondition.entries
										.filter { it.ordinal <= condition.ordinal }
										.map { it.name }
										.toSet()
									selectedItems = selectedItems + condition
								}
							}
						}
						.padding(12.dp),
					verticalAlignment = Alignment.CenterVertically
				) {

					ConditionTag(shortName, color)

					Spacer(modifier = Modifier.width(8.dp))

					Text(
						name,
						style = MaterialTheme.typography.bodyLarge,
						modifier = Modifier.weight(1f)
					)
				}
			}
			Spacer(modifier = Modifier.height(42.dp))
		}

		FloatingButton(
			"Select",
			selectedItems.isNotEmpty()
		) { onChoose(selectedItems) }
	}
}

@Composable
private fun ConditionTag(text: String, color: Color) {
	Box(
		modifier = Modifier
			.width(32.dp)
			.height(32.dp)
			.clip(RoundedCornerShape(4.dp))
			.background(color),
		contentAlignment = Alignment.Center
	) {
		Text(text, color = Color.White, style = TextStyle(fontSize = 18.sp))
	}
}

private fun CardCondition.menuOptions(): Triple<String, Color, String> = when (this) {
	CardCondition.Mint -> Triple("Mint", Color(0xFF7BB125), "MI")
	CardCondition.NearMint -> Triple("Near Mint", Color(0xFF7BB125), "NM")
	CardCondition.SlightlyPlayed -> Triple("Slightly Played", Color(0xFFA5B200), "SP")
	CardCondition.ModeratelyPlayed -> Triple("Moderately Played", Color(0xFFE39101), "MP")
	CardCondition.Played -> Triple("Played", Color(0xFFFD8428), "PL")
	CardCondition.HeavilyPlayed -> Triple("Heavily Played", Color(0xFF9C3333), "HP")
	CardCondition.Poor -> Triple("Poor", Color(0xFF9C3333), "PO")
}