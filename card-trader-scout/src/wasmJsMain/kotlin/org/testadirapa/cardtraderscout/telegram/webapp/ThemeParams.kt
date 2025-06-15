package org.testadirapa.cardtraderscout.telegram.webapp

typealias HEXColor = String

external interface ThemeParams {
	@JsName("bg_color")
	val backgroundColor: HEXColor?
	@JsName("secondary_bg_color")
	val secondaryBackgroundColor: HEXColor?
	@JsName("text_color")
	val textColor: HEXColor?
	@JsName("hint_color")
	val hintColor: HEXColor?
	@JsName("link_color")
	val linkColor: HEXColor?
	@JsName("button_color")
	val buttonColor: HEXColor?
	@JsName("button_text_color")
	val buttonTextColor: HEXColor?
}