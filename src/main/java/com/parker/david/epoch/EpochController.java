package com.parker.david.epoch;

import com.parker.david.SearchMetaInfo;

/**
 * controls when new epochs are triggered
 */
public interface EpochController {

	/**
	 * boolean true if conditions for a new epoch are satisfied
	 *
	 * @param searchInfo the search space info
	 * @return a boolean. true if new epoch
	 */
	boolean newEpochTriggered(SearchMetaInfo searchInfo);
}
