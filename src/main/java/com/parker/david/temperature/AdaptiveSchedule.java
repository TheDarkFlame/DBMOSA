package com.parker.david.temperature;

import com.parker.david.SearchMetaInfo;

/**
 * apative cooling schedule based on the linear schedule
 * temperature changes by factor * epochfactor ^ (epoch)
 */
public class AdaptiveSchedule extends LinearSchedule {
	private double epochFactor;

	/**
	 * constructor
	 *
	 * @param epochFactor      the factor by which we decrease the effect of the increment with more epochs
	 * @param coolingIncrement the amount we cool by
	 * @param heatingIncrement the amount we heat by
	 */
	public AdaptiveSchedule(double coolingIncrement, double heatingIncrement, double epochFactor) {
		super(coolingIncrement, heatingIncrement);
		this.epochFactor = (epochFactor > 0) ? epochFactor : 1; //check for 0
	}

	/**
	 * cool by a fixed amount
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void cool(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(searchMeta.getTemperature() - coolingIncrement * epochFactor);
		epochFactor *= epochFactor;
	}

	/**
	 * heat by a fixed amount
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void heat(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(searchMeta.getTemperature() + heatingIncrement * epochFactor);
		epochFactor *= epochFactor;
	}
}