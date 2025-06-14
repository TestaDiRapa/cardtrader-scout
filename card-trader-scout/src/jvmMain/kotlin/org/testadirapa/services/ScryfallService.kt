package org.testadirapa.services

import org.testadirapa.scryfall.PaginatedList
import org.testadirapa.scryfall.ScryfallCard

interface ScryfallService {

	/**
	 * Queries scryfall returning a [PaginatedList] of cards.
	 *
	 * @param query a plain-text query, that can be in the scryfall query language.
	 * @return a [PaginatedList] of [ScryfallCard].
	 * @throws IllegalArgumentException if the [query] length is less than 3 or greater than 999.
	 */
	suspend fun searchCards(query: String): PaginatedList<ScryfallCard>

}