package com.parker.david;

import java.util.ArrayList;

/**
 * generate a <b>feasible</b> solution in the neighbourhood of the current solution
 */
public interface SolutionFromNeighbourhoodGenerator {
	CandidateSolution neighbourhoodSolution(CandidateSolution originalSolution);
}
