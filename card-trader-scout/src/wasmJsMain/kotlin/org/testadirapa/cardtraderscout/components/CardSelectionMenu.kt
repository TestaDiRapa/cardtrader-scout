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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.testadirapa.cardtrader.Blueprint
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID
import org.testadirapa.cardtraderscout.utils.toProxiedImageUrl
import org.testadirapa.scryfall.ScryfallCard

@Composable
inline fun <reified T> CardSelectionMenu(
	colorScheme: ColorScheme,
	webApp: SimpleWebApp,
	baseUrl: String,
	token: String,
	items: Map<String, List<T>>,
	includeAllOption: Boolean,
	labelSelector: (String, List<T>) -> String,
	crossinline onChoose: (Set<String>) -> Unit,
) {
	val scrollState = rememberScrollState()
	var selectedItemId by remember { mutableStateOf("") }
	var selectedItems by remember { mutableStateOf<Set<String>>(emptySet()) }

	Box(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(scrollState)
				.padding(bottom = 72.dp)
		) {
			if (includeAllOption) {
				val isSelected = selectedItemId == ALL_INTERNAL_ID
				val backgroundColor = if (isSelected) colorScheme.surfaceContainerHigh else colorScheme.tertiary
				val borderColor = if (isSelected) colorScheme.surfaceContainerHigh else Color.LightGray

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 4.dp)
						.clip(RoundedCornerShape(16.dp))
						.border(1.dp, borderColor, RoundedCornerShape(16.dp))
						.background(backgroundColor)
						.clickable {
							selectedItemId = ALL_INTERNAL_ID
							selectedItems = items.keys
						}
						.padding(12.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					RadioButton(
						selected = isSelected,
						onClick = { selectedItemId = ALL_INTERNAL_ID }
					)

					Spacer(modifier = Modifier.width(8.dp))

					Text(
						"Select all",
						style = MaterialTheme.typography.bodyLarge,
						modifier = Modifier.weight(1f),
						color = colorScheme.onBackground,
					)
				}
			}

			items.forEach { (selector, cards) ->
				val isSelected = selector == selectedItemId
				val backgroundColor = if (isSelected) colorScheme.surfaceContainerHigh else colorScheme.tertiary
				val borderColor = if (isSelected) colorScheme.surfaceContainerHigh else Color.LightGray

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 4.dp)
						.clip(RoundedCornerShape(16.dp))
						.border(1.dp, borderColor, RoundedCornerShape(16.dp))
						.background(backgroundColor)
						.clickable {
							selectedItemId = selector
							selectedItems = setOf(selector)
						}
						.padding(12.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					RadioButton(
						selected = isSelected,
						onClick = {
							selectedItemId = selector
							selectedItems = setOf(selector)
						}
					)

					Spacer(modifier = Modifier.width(8.dp))

					Text(
						labelSelector(selector, cards),
						style = MaterialTheme.typography.bodyLarge,
						modifier = Modifier.weight(1f),
						color = colorScheme.onBackground,
					)

					Row(
						horizontalArrangement = Arrangement.spacedBy(2.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						val cardUrls = cards.mapNotNull { it.getImageUrl(baseUrl, token) }
						cardUrls.take(2).forEach { url ->
							AsyncImage(
								model = url,
								contentDescription = null,
								modifier = Modifier.size(64.dp),
								contentScale = ContentScale.Fit,
							)
						}
						if (cardUrls.size >= 3) {
							Icon(
								imageVector = Icons.Default.MoreHoriz,
								contentDescription = "More",
								tint = MaterialTheme.colorScheme.primary,
							)
						}
					}
				}
			}
			Spacer(modifier = Modifier.height(42.dp))
		}

		FloatingButton(
			webApp,
			"Select",
			selectedItems.isNotEmpty()
		) { onChoose(selectedItems) }
	}
}


@Composable
inline fun <reified T> T.getImageUrl(baseUrl: String, token: String): String? = when (this) {
	is Blueprint -> image?.preview?.toProxiedImageUrl(baseUrl, token)
	is ScryfallCard -> imageUris?.small
	else -> null
}
