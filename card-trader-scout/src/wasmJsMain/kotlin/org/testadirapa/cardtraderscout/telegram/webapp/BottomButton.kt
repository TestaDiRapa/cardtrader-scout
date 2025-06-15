package org.testadirapa.cardtraderscout.telegram.webapp

external class BottomButton {
	val text: String
	fun setText(text: String): BottomButton

	var color: String
	var textColor: String

	val isVisible: Boolean
	fun show(): BottomButton
	fun hide(): BottomButton

	val isActive: Boolean
	fun enable(): BottomButton
	fun disable(): BottomButton

	val isProgressVisible: Boolean
	fun showProgress(leaveActive: Boolean = definedExternally): BottomButton
	fun hideProgress(): BottomButton

	fun onClick(eventHandler: () -> Unit): BottomButton
	fun offClick(eventHandler: () -> Unit): BottomButton
}