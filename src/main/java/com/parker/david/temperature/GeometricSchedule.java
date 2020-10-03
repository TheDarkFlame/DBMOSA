package com.parker.david.temperature;

import com.parker.david.SearchMetaInfo;

/**
 * a geometric cooling schedule. So newTemp = coefficient * oldTemp
 */
public class GeometricSchedule implements TemperatureController {

	/**
	 * the cooling coefficient
	 */
	private final double coolingCoefficient;

	/**
	 * the heating coefficient
	 */
	private final double heatingCoefficient;

	/**
	 * constructor
	 *
	 * @param coolingCoefficient set cooling coefficient
	 * @param heatingCoefficient set heating coefficient
	 */
	public GeometricSchedule(double coolingCoefficient, double heatingCoefficient) {
		this.coolingCoefficient = coolingCoefficient;
		this.heatingCoefficient = heatingCoefficient;
	}

	/**
	 * cool by a factor
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void cool(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(searchMeta.getTemperature() * coolingCoefficient);
	}

	/**
	 * heat by a factor
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void heat(SearchMetaInfo searchMeta) {
		searchMeta.setTemperature(searchMeta.getTemperature() * heatingCoefficient);
	}
}
