package com.parker.david.initialisation;

import com.parker.david.CandidateSolution;
import com.parker.david.ObjectiveSet;
import com.parker.david.SolutionFromNeighbourhoodGenerator;

/**
 * should implement an approximated initialisation metric. Given the continuous nature of the problem, an exact metric may not be possible
 */
public interface InitialTemperatureAssignment {

	/**
	 * gets an initial temperature for the search
	 *
	 * @param initialSolution the first solution from where the search will kick off
	 * @param objectives      the set of objectives for which the solution aims to optimise
	 * @param generator       the next solution generation method
	 * @return double, the initial temperature for the algorithm
	 */
	double getInitialTemperature(CandidateSolution initialSolution, ObjectiveSet objectives, SolutionFromNeighbourhoodGenerator generator);
}
