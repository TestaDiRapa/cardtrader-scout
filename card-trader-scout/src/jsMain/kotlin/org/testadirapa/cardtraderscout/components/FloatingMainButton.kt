package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import dev.inmo.tgbotapi.webapps.BottomButtonParams
import dev.inmo.tgbotapi.webapps.haptic.HapticFeedbackType
import dev.inmo.tgbotapi.webapps.setParams
import dev.inmo.tgbotapi.webapps.webApp

@Composable
fun FloatingMainButton(
	text: String = "Select",
	show: Boolean,
	color: String? = null,
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
		if (color != null) {
			setParams(BottomButtonParams(
				color = color
			))
		}
	}
}