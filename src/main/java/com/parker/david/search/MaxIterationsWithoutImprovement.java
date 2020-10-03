package com.parker.david.search;

import com.parker.david.SearchMetaInfo;

/**
 * checks for a max number of iterations without improvement of the archive
 */
public class MaxIterationsWithoutImprovement implements TerminationController {

	/**
	 * the max number of improvements needed to terminate the search
	 */
	final private int maxIterationsWithoutImprovement;

	/**
	 * constructor
	 *
	 * @param maxIterationsWithoutImprovement the max number of improvements needed for termination
	 */
	public MaxIterationsWithoutImprovement(int maxIterationsWithoutImprovement) {
		this.maxIterationsWithoutImprovement = maxIterationsWithoutImprovement;
	}

	/**
	 * a check for if the search may continue, checks if we have hit the threshold number of iterations without a better archive
	 *
	 * @param searchMeta the search progress information
	 * @return a boolean. False if search should terminate
	 */
	@Override
	public boolean continueSearch(SearchMetaInfo searchMeta) {
		return searchMeta.getIterationsWithoutAddingToArchive() < maxIterationsWithoutImprovement;
	}
}
