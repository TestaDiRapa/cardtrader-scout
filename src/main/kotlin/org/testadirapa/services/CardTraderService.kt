package org.testadirapa.services

import org.testadirapa.models.cardtrader.Blueprint
import org.testadirapa.models.cardtrader.Expansion
import org.testadirapa.models.cardtrader.MtgLanguage
import org.testadirapa.models.cardtrader.Product

interface CardTraderService {

	/**
	 * Returns the [Expansion] for the provided [code], if any is found. The full expansion list from CardTrader is
	 * cached every hour.
	 *
	 * @param code the code of the expansion.
	 * @return an [Expansion] or null if no expansion with the provided [code] is found.
	 */
	suspend fun getExpansionByCode(code: String): Expansion?

	/**
	 * Returns all the [Blueprint]s for the provided name. If [expansionId] is not null, only the [Blueprint]s for that
	 * expansion are returned.
	 *
	 * @param name the name of the blueprint to search.
	 * @param expansionId the id of the expansion, if known.
	 * @return a [List] of [Blueprint].
	 */
	suspend fun searchBlueprintsByNameAndExpansionId(name: String, expansionId: Int?): List<Blueprint>

	/**
	 * Returns all the listings on CardTrader for all the listings of the specified blueprint.
	 *
	 * @param blueprintId the id of the blueprint to search.
	 * @param expansionId if not null, filters out all the products that are not of the specified expansion.
	 * @param foil if true, only returns the listings of foil products.
	 * @param language if not null, only returns the products with the specified language.
	 * @return a [List] of [Product] that match all the filters.
	 */
	suspend fun searchProducts(
		blueprintId: String,
		expansionId: String? = null,
		foil: Boolean = false,
		language: MtgLanguage? = null
	): List<Product>
}