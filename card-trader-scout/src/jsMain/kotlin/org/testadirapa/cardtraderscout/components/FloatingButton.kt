package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import dev.inmo.tgbotapi.webapps.BottomButton
import dev.inmo.tgbotapi.webapps.BottomButtonParams
import dev.inmo.tgbotapi.webapps.haptic.HapticFeedbackType
import dev.inmo.tgbotapi.webapps.setParams
import dev.inmo.tgbotapi.webapps.webApp
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color

@Composable
fun FloatingMainButton(
	text: String = "Select",
	show: Boolean,
	color: CSSColorValue? = webApp.themeParams.buttonColor?.let { Color(it) },
	textColor: CSSColorValue? = null,
	onClick: () -> Unit,
) = FloatingButton(webApp.mainButton, text, show, color, textColor, onClick)

@Composable
fun FloatingSecondaryButton(
	text: String = "Select",
	show: Boolean,
	color: CSSColorValue? = null,
	textColor: CSSColorValue? = null,
	onClick: () -> Unit,
) = FloatingButton(webApp.secondaryButton, text, show, color, textColor, onClick)

@Composable
private fun FloatingButton(
	button: BottomButton,
	text: String = "Select",
	show: Boolean,
	color: CSSColorValue? = null,
	textColor: CSSColorValue? = null,
	onClick: () -> Unit,
) {
	button.apply {
		onClick {
			webApp.hapticFeedback.notificationOccurred(
				HapticFeedbackType.Success
			)
			onClick()
		}
		if (show) {
			enable()
			show()
		} else {
			disable()
			hide()
		}
		setParams(
			BottomButtonParams(
				color = color?.toString(),
				textColor = textColor?.toString(),
				text = text,
			)
		)
	}
}