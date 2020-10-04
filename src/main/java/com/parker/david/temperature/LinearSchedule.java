package com.parker.david.temperature;

import com.parker.david.SearchMetaInfo;

/**
 * control the temperature in a linear way, decreasing and increasing by a pre-defined amount
 */
public class LinearSchedule implements TemperatureController {
	/**
	 * the amount we heat by upon changing temperature
	 */
	protected final double heatingIncrement;
	/**
	 * the amount we cool by upon changing temperature
	 */
	protected final double coolingIncrement;

	/**
	 * the minimum temperature that we may have
	 */
	protected final double minTemperature;

	/**
	 * constructor
	 *
	 * @param coolingIncrement the amount we cool by
	 * @param heatingIncrement the amount we heat by
	 * @param minTemperature   the minimum temperature
	 */
	public LinearSchedule(double coolingIncrement, double heatingIncrement, double minTemperature) {
		this.coolingIncrement = coolingIncrement;
		this.heatingIncrement = heatingIncrement;
		this.minTemperature = minTemperature;
	}

	/**
	 * cool by a fixed amount
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void cool(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(Math.max(searchMeta.getTemperature() - coolingIncrement, minTemperature));
	}

	/**
	 * heat by a fixed amount
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void heat(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(searchMeta.getTemperature() + heatingIncrement);
	}
}
