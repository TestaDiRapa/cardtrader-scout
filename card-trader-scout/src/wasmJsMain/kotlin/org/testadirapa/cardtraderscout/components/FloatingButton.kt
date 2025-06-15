package org.testadirapa.cardtraderscout.components

import androidx.compose.runtime.Composable
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp

@Composable
fun FloatingButton(
	webApp: SimpleWebApp,
	text: String = "Select",
	show: Boolean,
	onClick: () -> Unit,
) {
	webApp.mainButton.apply {
		setText(text)
		onClick {
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