package com.parker.david.search;

import com.parker.david.SearchMetaInfo;

public class MinimumFinalTemperature implements TerminationController {
	final double minTemperature;

	public MinimumFinalTemperature(double terminationMinimumTemperature) {
		minTemperature = terminationMinimumTemperature;
	}

	@Override
	public boolean continueSearch(SearchMetaInfo searchMeta) {
		return searchMeta.getTemperature() > minTemperature;
	}
}
