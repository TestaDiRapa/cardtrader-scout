package org.testadirapa.cardtraderscout.telegram.webapp

external class SimpleWebApp {
	val initData: String
	val initDataUnsafe: WebAppInitData
	@JsName("MainButton")
	val mainButton: BottomButton
	val colorScheme: String
	val themeParams: ThemeParams

	fun close()
}