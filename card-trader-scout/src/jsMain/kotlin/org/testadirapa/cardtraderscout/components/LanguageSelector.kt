package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.gridTemplateColumns
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.testadirapa.cardtrader.MtgLanguage
import org.testadirapa.cardtraderscout.theme.ColorTheme
import org.testadirapa.cardtraderscout.utils.ALL_INTERNAL_ID

@Composable
fun LanguageSelector(
	colorScheme: ColorTheme.ColorSchemeParams,
	initialValues: Set<MtgLanguage> = emptySet(),
	onCancel: (() -> Unit)? = null,
	onChoose: (Set<MtgLanguage>) -> Unit,
) {
	var selectedIds by remember {
		mutableStateOf(
			if (initialValues == MtgLanguage.entries.toSet())
				setOf(ALL_INTERNAL_ID)
			else initialValues.map { it.name }.toSet()
		)
	}
	var selectedLanguages by remember { mutableStateOf(initialValues) }

	Title(
		colorScheme = colorScheme,
		text = "Choose the Language(s)"
	)

	Div({
		style {
			display(DisplayStyle.Grid)
			gridTemplateColumns("repeat(2, 1fr)")
			gap(8.px)
		}
	}) {
		LanguageElement(
			colorScheme = colorScheme,
			emoji = "🇺🇳",
			language = "Any",
			isSelected = selectedIds.contains(ALL_INTERNAL_ID),
			onClick = {
				selectedIds = setOf(ALL_INTERNAL_ID)
				selectedLanguages = MtgLanguage.entries.toSet()
			}
		)

		MtgLanguage.entries.forEach { language ->
			val (emoji, name) = language.toSelectorParameters()
			LanguageElement(
				colorScheme = colorScheme,
				emoji = emoji,
				language = name,
				isSelected = selectedIds.contains(language.name),
				onClick = {
					when {
						selectedIds.contains(ALL_INTERNAL_ID) -> {
							selectedIds = setOf(language.name)
							selectedLanguages = setOf(language)
						}
						selectedIds.contains(language.name) -> {
							selectedIds = selectedIds - language.name
							selectedLanguages = selectedLanguages - language
						}
						else -> {
							selectedIds = selectedIds + language.name
							selectedLanguages = selectedLanguages + language
						}
					}
				}
			)
		}
	}
	if (onCancel != null) {
		FloatingSecondaryButton(
			text = "Cancel",
			show = true,
			color = colorScheme.secondaryButtonColor,
			textColor = Color("#FFFFFF"),
			onClick = onCancel
		)
	}
	FloatingMainButton(
		text = "Select",
		show = selectedLanguages.isNotEmpty()
	) {
		val languages = selectedLanguages
		if (languages.isNotEmpty()) {
			selectedLanguages = emptySet()
			selectedIds = emptySet()
			onChoose(languages)
		}

	}
}

@Composable
fun LanguageElement(
	colorScheme: ColorTheme.ColorSchemeParams,
	emoji: String,
	language: String,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	val backgroundColor = if (isSelected) colorScheme.selectedBackground else colorScheme.unSelectedBackground
	val borderColor = if (isSelected) colorScheme.selectedBackground else Color.lightgray

	Div({
		onClick { onClick() }
		style {
			display(DisplayStyle.Flex)
			alignItems(AlignItems.Center)
			gap(8.px)
			backgroundColor(backgroundColor)
			border(1.px, LineStyle.Solid, borderColor)
			padding(12.px)
			borderRadius(16.px)
			fontSize(16.px)
			color(colorScheme.textColor)
		}
	}) {
		Span({ style {
			fontSize(24.px)
			fontFamily("NotoColorEmoji")
		} }) { Text(emoji) }

		Span({ style { flexGrow(1.0) } }) { Text(language) }
	}
}

fun MtgLanguage.toSelectorParameters(): Pair<String, String> = when(this) {
	MtgLanguage.De -> "🇩🇪" to "German"
	MtgLanguage.En -> "🇬🇧" to "English"
	MtgLanguage.Es -> "🇪🇸" to "Spanish"
	MtgLanguage.Fr -> "🇫🇷" to "French"
	MtgLanguage.It -> "🇮🇹" to "Italian"
	MtgLanguage.Jp -> "🇯🇵" to "Japanese"
	MtgLanguage.Pt -> "🇵🇹" to "Portuguese"
	MtgLanguage.Cn -> "🇨🇳" to "Chinese"
}