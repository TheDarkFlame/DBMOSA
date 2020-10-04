package com.parker.david;

import java.util.ArrayList;

/**
 * a factory for generating new candidate solutions, keeps track of the objective functions and the id of the solution
 */
public class CandidateSolutionFactory {

	/**
	 * the number of digits behind the decimal place which doubles may have
	 */
	int doubleDecimalRoundOff;

	/**
	 * a counter for the solution's ID. designed for easier comparison of solutions, never actually used for anything useful
	 */
	private int solutionCounter = 0;

	/**
	 * a set of objectives that we wish to satisfy
	 */
	ObjectiveSet objectives;

	/**
	 * a function to get a new candidate solution given a set of decision variables
	 *
	 * @param decisionVariables the set of decision variables that define this candidate solution
	 */
	public CandidateSolution getCandidateSolution(ArrayList<Double> decisionVariables) {
		ArrayList<Double> rounded = new ArrayList<>();
		for (Double decisionVariable : decisionVariables) {
			rounded.add(round(decisionVariable));
		}
		return new CandidateSolution(rounded, solutionCounter++, this);
	}

	/**
	 * rounds a number based on the defined decimal roundoff
	 */
	private double round(double number) {
		double factor = Math.pow(10, doubleDecimalRoundOff);
		return Math.round(number * factor) / factor;
	}

	/**
	 * constructor
	 *
	 * @param doubleDecimalRoundOff the number of digits after the decimal for doubles
	 * @param objectives            the objective function set that the solutions aim to optimise
	 */

	CandidateSolutionFactory(ObjectiveSet objectives, int doubleDecimalRoundOff) {
		this.objectives = objectives;
		this.doubleDecimalRoundOff = doubleDecimalRoundOff;
	}

}
