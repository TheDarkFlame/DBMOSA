package com.parker.david.search;

import com.parker.david.SearchMetaInfo;

/**
 * a condition that determines when a search may terminate
 */
public interface TerminationController {

	/**
	 * a check on the search information to see if search should continue
	 *
	 * @param searchMeta the search progress information
	 * @return a boolean. False if search should terminate
	 */
	boolean continueSearch(SearchMetaInfo searchMeta);
}
