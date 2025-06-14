package org.testadirapa.models.db

interface StoredDocument {
	val id: String
	val rev: String?
	val descriptor: String
}