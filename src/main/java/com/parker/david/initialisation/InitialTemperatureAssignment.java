package com.parker.david.initialisation;

import com.parker.david.CandidateSolution;
import com.parker.david.ObjectiveSet;
import com.parker.david.SolutionFromNeighbourhoodGenerator;

/**
 * should implement an approximated initialisation metric. Given the continuous nature of the problem, an exact metric may not be possible
 */
public interface InitialTemperatureAssignment {
	double getInitialTemperature(CandidateSolution initialSolution, ObjectiveSet objectives, SolutionFromNeighbourhoodGenerator generator);
}
