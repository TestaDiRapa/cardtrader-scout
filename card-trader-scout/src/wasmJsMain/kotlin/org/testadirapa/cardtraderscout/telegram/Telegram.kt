package org.testadirapa.cardtraderscout.telegram

import kotlinx.browser.window
import org.testadirapa.cardtraderscout.telegram.webapp.SimpleWebApp
import org.w3c.dom.Window
import org.w3c.dom.get

external interface Telegram : JsAny {
	val WebApp: SimpleWebApp
}

val Window.Telegram: Telegram?
	get() = window["Telegram"]?.unsafeCast()

val telegram: Telegram?
	get() = window.Telegram

val webApp: SimpleWebApp?
	get() = telegram?.WebApp