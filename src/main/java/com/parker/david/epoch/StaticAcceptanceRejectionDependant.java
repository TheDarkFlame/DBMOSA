package com.parker.david.epoch;

import com.parker.david.SearchMetaInfo;

/**
 * a static length epoch controller, based on the number of acceptances or rejections, defined before search begins
 */
public class StaticAcceptanceRejectionDependant implements EpochController {

	/**
	 * max permitted acceptances before new epoch is triggered
	 */
	int maxAcceptances;

	/**
	 * max permitted rejections before new epoch is triggered
	 */
	int maxRejections;

	/**
	 * constructor
	 *
	 * @param maxAcceptances the max acceptances before epoch trigger
	 * @param maxRejections  the max rejections before epoch trigger
	 */
	public StaticAcceptanceRejectionDependant(int maxAcceptances, int maxRejections) {
		this.maxAcceptances = maxAcceptances;
		this.maxRejections = maxRejections;
	}

	/**
	 * determines whether a new epoch is triggered based on the current search info
	 *
	 * @param searchInfo the current search progress information
	 * @return boolean. true if new epoch
	 */
	@Override
	public boolean newEpochTriggered(SearchMetaInfo searchInfo) {
		return searchInfo.getAcceptances() >= maxAcceptances || searchInfo.getRejections() >= maxRejections;
	}
}
