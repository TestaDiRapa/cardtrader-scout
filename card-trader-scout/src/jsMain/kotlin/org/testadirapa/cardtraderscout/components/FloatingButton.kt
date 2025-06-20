package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import dev.inmo.tgbotapi.webapps.haptic.HapticFeedbackType
import dev.inmo.tgbotapi.webapps.webApp

@Composable
fun FloatingButton(
	text: String = "Select",
	show: Boolean,
	onClick: () -> Unit,
) {
	webApp.mainButton.apply {
		setText(text)
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
	}
}