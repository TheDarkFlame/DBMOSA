package com.parker.david.initialisation;

import com.parker.david.*;
import com.parker.david.Neighbourhood.SolutionFromNeighbourhoodGenerator;

import java.util.ArrayList;

/**
 * an initial temperature setting that aims to have the first generated solution accepted
 * since a continuous solution has an infinite neighbourhood, this is an approximation.
 * Temperature is high enough when we hit a pre-defined number of neighbour acceptances in a row at a temperature
 */
public class AcceptAll implements InitialTemperatureAssignment {

	/**
	 * the coefficient by which the initial temperature increases each time when it is too cool
	 */
	private static double increaseCoefficient = 1.2;

	/**
	 * the number of solutions that need to be accepted in a row to satisfy the search
	 */
	private final int searchTerminator;

	/**
	 * a set of solutions which are not necessarily in the neighbourhood, but we we want to accept them,
	 * as they have some significant importance to the problem itself
	 */
	private ArrayList<CandidateSolution> testSolutions;

	/**
	 * constructor
	 *
	 * @param acceptedSearchTerminationCount this is the number of solutions that must be accepted without a rejection to terminate the search for intial T
	 * @param definedComparisonSolutions     a list of significant solutions which we seek to accept
	 */
	public AcceptAll(int acceptedSearchTerminationCount, ArrayList<CandidateSolution> definedComparisonSolutions) {
		this.searchTerminator = acceptedSearchTerminationCount;
		this.testSolutions = definedComparisonSolutions;
	}

	/**
	 * algorithm to get initial temperature.
	 * it works by checking if a solution would be accepted, if yes, continue, if no, increase temperature until it does.
	 * do this for the set of significant solutions, and then randomly for a number of neighbours
	 *
	 * @param initialSolution the first solution from where the search will kick off
	 * @param objectives      the set of objectives for which the solution aims to optimise
	 * @param generator       the next solution generation method
	 * @return double, the initial temperature for the algorithm
	 */
	@Override
	public double getInitialTemperature(CandidateSolution initialSolution, ObjectiveSet objectives, SolutionFromNeighbourhoodGenerator generator) {

		//empty archive
		SolutionArchive emptyArchive = new SolutionArchive();

		//test given specific solutions
		double temperature = 0.005;
		for (CandidateSolution testSolution : testSolutions) {
			//while our temperature is too cool, increase the temperature
			while (!AcceptanceComputer.isAccepted(emptyArchive, initialSolution, testSolution, temperature)) {
				temperature *= increaseCoefficient;
			}
		}

		//test against randomly generated solutions, we must get a set number of acceptances without rejections
		CandidateSolution randomNeighbour = generator.neighbourhoodSolution(initialSolution);
		int acceptedCounter = 0;
		while (acceptedCounter < searchTerminator) {

			//if rejected, reset the counter and increase temperature until accepted
			if (!AcceptanceComputer.isAccepted(emptyArchive, initialSolution, randomNeighbour, temperature)) {
				acceptedCounter = 0;
				while (!AcceptanceComputer.isAccepted(emptyArchive, initialSolution, randomNeighbour, temperature)) {
					temperature *= increaseCoefficient;
				}
			}

			//increase acceptances, and generate a new neighbour for the next test
			acceptedCounter++;
			randomNeighbour = generator.neighbourhoodSolution(initialSolution);
		}
		return temperature;
	}
}
