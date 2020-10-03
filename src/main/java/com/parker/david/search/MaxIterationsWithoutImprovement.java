package com.parker.david.search;

import com.parker.david.SearchMetaInfo;

public class MaxIterationsWithoutImprovement implements TerminationController {

	final private int maxIterationsWithoutImprovement;

	public MaxIterationsWithoutImprovement(int maxIterationsWithoutImprovement) {
		this.maxIterationsWithoutImprovement = maxIterationsWithoutImprovement;
	}

	@Override
	public boolean continueSearch(SearchMetaInfo searchMeta) {
		return searchMeta.getIterationsWithoutAddingToArchive() < maxIterationsWithoutImprovement;
	}
}
