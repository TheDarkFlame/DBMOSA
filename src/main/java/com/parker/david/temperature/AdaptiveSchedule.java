package com.parker.david.temperature;

import com.parker.david.SearchMetaInfo;

/**
 * adaptive cooling schedule based on the linear schedule
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
	 * @param minTemperature   the minimum temperature
	 */
	public AdaptiveSchedule(double coolingIncrement, double heatingIncrement, double minTemperature, double epochFactor) {
		super(coolingIncrement, heatingIncrement, minTemperature);
		this.epochFactor = (epochFactor > 0) ? epochFactor : 1; //check for 0
	}

	/**
	 * cool by a fixed amount
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void cool(SearchMetaInfo searchMeta) {
		double newTemperature = searchMeta.getTemperature() - coolingIncrement * epochFactor;
		if (newTemperature > minTemperature) {
			searchMeta.setTemperature(newTemperature);
			epochFactor *= epochFactor;
		} else {
			searchMeta.setTemperature(minTemperature);
		}
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
