package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID

@Composable
fun LanguageSelector(
	colorScheme: ColorScheme,
	webApp: SimpleWebApp,
	onChoose: (Set<MtgLanguage>) -> Unit,
) {
	var selectedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
	var selectedLanguages by remember { mutableStateOf<Set<MtgLanguage>>(emptySet()) }
	Box(modifier = Modifier.fillMaxSize()) {
		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center,
			) {
				Text(
					text = "Choose the language(s)",
					style = MaterialTheme.typography.headlineMedium,
					color = colorScheme.onBackground,
				)
			}
			LazyVerticalGrid(
				columns = GridCells.Adaptive(minSize = 160.dp),
				modifier = Modifier.fillMaxSize().padding(8.dp),
				contentPadding = PaddingValues(8.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				item {
					LanguageElement(
						colorScheme = colorScheme,
						emoji = "🎌",
						language = "Any",
						isSelected = selectedIds.contains(ALL_INTERNAL_ID),
					) {
						selectedIds = setOf(ALL_INTERNAL_ID)
						selectedLanguages = MtgLanguage.entries.toSet()
					}
				}
				items(MtgLanguage.entries) { language ->
					val (emoji, languageName) = language.toSelectorParameters()
					LanguageElement(
						colorScheme = colorScheme,
						emoji = emoji,
						language = languageName,
						isSelected = selectedIds.contains(language.name),
					) {
						when {
							selectedIds.contains(ALL_INTERNAL_ID) -> {
								selectedIds = setOf(language.name)
								selectedLanguages = setOf(language)
							}
							selectedIds.contains(language.name) -> {
								selectedIds =  selectedIds - language.name
								selectedLanguages = selectedLanguages - language
							}
							else -> {
								selectedIds =  selectedIds + language.name
								selectedLanguages = selectedLanguages + language
							}
						}
					}
				}
			}
		}
		Spacer(modifier = Modifier.height(42.dp))
		FloatingButton(
			webApp,
			text = "Select",
			show = selectedIds.isNotEmpty()
		) { onChoose(selectedLanguages) }
	}
}

@Composable
private fun LanguageElement(
	colorScheme: ColorScheme,
	emoji: String,
	language: String,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	val backgroundColor = if (isSelected) colorScheme.surfaceContainerHigh else colorScheme.tertiary
	val borderColor = if (isSelected) colorScheme.surfaceContainerHigh else Color.LightGray
	Row(
		modifier = Modifier
			.clip(RoundedCornerShape(16.dp))
			.background(backgroundColor)
			.border(1.dp, borderColor, RoundedCornerShape(16.dp))
			.clickable(onClick = onClick)
			.padding(12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
//		Text(
//			text = emoji,
//			fontSize = 24.sp,
//			textAlign = TextAlign.Center
//		)
//		Spacer(modifier = Modifier.width(8.dp))
		Text(
			language,
			style = MaterialTheme.typography.bodyLarge,
			modifier = Modifier.weight(1f),
			color = colorScheme.onBackground,
		)
	}
}

private fun MtgLanguage.toSelectorParameters(): Pair<String, String> = when(this) {
	MtgLanguage.De -> "🇩🇪" to "German"
	MtgLanguage.En -> "🇬🇧" to "English"
	MtgLanguage.Es -> "🇪🇸" to "Spanish"
	MtgLanguage.Fr -> "🇫🇷" to "French"
	MtgLanguage.It -> "🇮🇹" to "Italian"
	MtgLanguage.Jp -> "🇯🇵" to "Japanese"
	MtgLanguage.Pt -> "🇵🇹" to "Portuguese"
}
