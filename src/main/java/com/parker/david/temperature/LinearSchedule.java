package com.parker.david.temperature;

import com.parker.david.SearchMetaInfo;

/**
 * control the temperature in a linear way, decreasing and increasing by a pre-defined amount
 * */
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
	 * constructor
	 *
	 * @param coolingIncrement the amount we cool by
	 * @param heatingIncrement the amount we heat by
	 */
	public LinearSchedule(double coolingIncrement, double heatingIncrement) {
		this.coolingIncrement = coolingIncrement;
		this.heatingIncrement = heatingIncrement;
	}

	/**
	 * cool by a fixed amount
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void cool(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(searchMeta.getTemperature() - coolingIncrement);
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
