package com.parker.david.initialisation;

import com.parker.david.*;

import java.util.ArrayList;

public class AcceptAll implements InitialTemperatureAssignment {

	private static double increaseCoefficient = 1.2;

	private final int searchTerminator;
	private ArrayList<CandidateSolution> testSolutions;

	/**
	 * @param acceptedSearchTerminationCount this is the number of solutions that must be accepted without a rejection to terminate the search for intial T
	 */
	public AcceptAll(int acceptedSearchTerminationCount, ArrayList<CandidateSolution> definedComparisonSolutions) {
		this.searchTerminator = acceptedSearchTerminationCount;
		this.testSolutions = definedComparisonSolutions;
	}

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
