package com.parker.david.temperature;

import com.parker.david.SearchMetaInfo;

/**
 * an interface for temperature controller module
 */
public interface TemperatureController {
	/**
	 * cool in some way
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	void cool(SearchMetaInfo searchMeta);

	/**
	 * heat in some way
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	void heat(SearchMetaInfo searchMeta);
}
