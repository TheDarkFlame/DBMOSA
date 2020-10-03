package com.parker.david.epoch;

import com.parker.david.SearchMetaInfo;

public class StaticLength implements EpochController {

	int maxAcceptances;
	int maxRejections;

	public StaticLength(int maxAcceptances, int maxRejections) {
		this.maxAcceptances = maxAcceptances;
		this.maxRejections = maxRejections;
	}

	@Override
	public boolean newEpochTriggered(SearchMetaInfo searchInfo) {
		return searchInfo.getAcceptances() >= maxAcceptances || searchInfo.getRejections() >= maxRejections;
	}
}
