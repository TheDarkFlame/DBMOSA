package com.parker.david.search;

import com.parker.david.SearchMetaInfo;

/**
 * minimum temperature termination criteria. If temperature drops below a set temperature the search is terminated.
 */
public class MinimumFinalTemperature implements TerminationController {

	/**
	 * the temperature threshold
	 */
	final double minTemperature;

	/**
	 * constructor
	 *
	 * @param terminationMinimumTemperature the minimum temperature, below which search terminates
	 */
	public MinimumFinalTemperature(double terminationMinimumTemperature) {
		minTemperature = terminationMinimumTemperature;
	}

	/**
	 * check if search may continue at the current temperature
	 *
	 * @param searchMeta the search progress information
	 * @return a boolean. False if search should terminate
	 */
	@Override
	public boolean continueSearch(SearchMetaInfo searchMeta) {
		return searchMeta.getTemperature() > minTemperature;
	}
}
