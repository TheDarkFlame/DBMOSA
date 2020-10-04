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
	 * the minimum temperature that we may have
	 */
	private final double minTemperature;

	/**
	 * constructor
	 *
	 * @param coolingCoefficient set cooling coefficient
	 * @param heatingCoefficient set heating coefficient
	 * @param minTemp            the min temperature
	 */
	public GeometricSchedule(double coolingCoefficient, double heatingCoefficient, double minTemp) {
		this.coolingCoefficient = coolingCoefficient;
		this.heatingCoefficient = heatingCoefficient;
		this.minTemperature = minTemp;
	}

	/**
	 * cool by a factor
	 *
	 * @param searchMeta the search meta, temperature will be updated
	 */
	@Override
	public void cool(SearchMetaInfo searchMeta) {
		//prevent going below mintemperature
		searchMeta.setTemperature(Math.max(searchMeta.getTemperature() * coolingCoefficient, minTemperature));
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
