package org.testadirapa.models.exceptions

class UnexpectedBodyException(
	message: String,
	private val actualBody: String
) : Exception(message), DispatchableException {
	override val messageToDispatch: String
		get() = "`\nmessage: $message\n$actualBody\n`"
}
