package com.parker.david;

/**
 * a data holding object that tracks the current state of the search
 */
public class SearchMetaInfo {
	/**
	 * the current epoch
	 */
	private int epoch;
	/**
	 * number of acceptances this epoch
	 */
	private int acceptances;
	/**
	 * number of rejections this epoch
	 */
	private int rejections;
	/**
	 * the current iteration
	 */
	private int iteration;
	/**
	 * the current temperature
	 */
	private double temperature;

	/**
	 * the number of iteration since last adding to the archive
	 */
	private int iterationsWithoutAddingToArchive;

	/**
	 * constructor
	 */
	SearchMetaInfo(double initialTemperature) {
		this.iterationsWithoutAddingToArchive = 0;
		this.epoch = 1;
	}

	/**
	 * reject this solution, return true if new epoch
	 */
	public void solutionRejection() {
		++iteration;
		iterationsWithoutAddingToArchive++;
		++rejections;
	}

	/**
	 * accept this solution, and if the solution was inserted into the archive,
	 * reset the counter on iterations without adding to the archive
	 *
	 * @param candidateInsertedIntoArchive boolean, true if solution was added to archive, false otherwise
	 */
	public void solutionAcceptance(boolean candidateInsertedIntoArchive) {
		++iteration;
		if (!candidateInsertedIntoArchive) iterationsWithoutAddingToArchive++;
		else iterationsWithoutAddingToArchive = 0;
		++acceptances;
	}

	/**
	 * set a new temperature
	 *
	 * @param temperature the new temperature
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * get the temperature
	 *
	 * @return the current temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * start a new epoch
	 */
	public void newEpoch() {
		++epoch;
		acceptances = 0;
		rejections = 0;
	}

	/**
	 * epoch getter
	 */
	public int getEpoch() {
		return epoch;
	}

	public int getIterationsWithoutAddingToArchive() {
		return iterationsWithoutAddingToArchive;
	}

	/**
	 * iteration getter
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * rejection count this epoch
	 */
	public int getRejections() {
		return rejections;
	}

	/**
	 * acceptance count this epoch
	 */
	public int getAcceptances() {
		return acceptances;
	}
}
