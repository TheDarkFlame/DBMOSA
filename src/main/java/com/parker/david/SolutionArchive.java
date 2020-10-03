package com.parker.david;

import java.util.ArrayList;

/**
 * the solution archive
 */
public class SolutionArchive {

	/**
	 * the internal list of optimal solutions
	 */
	private ArrayList<CandidateSolution> mostOptimalSolutions = new ArrayList<>();

	/**
	 * get the set of solutions in the archive
	 *
	 * @return the array list of solutions
	 */
	ArrayList<CandidateSolution> getArchive() {
		return mostOptimalSolutions;
	}

	/**
	 * attempt to add the solution to the archive. If the solution is not dominated by a solution in the archive,
	 * it gets added. When a new solution is added, any solutions that are dominated by the new solution are removed
	 * from the archive. Thus the archive is the set of non-dominated solutions. the Pareto Optimal set
	 *
	 * @param solution the solution which we attempt to insert into the archive
	 * @return true if solution was inserted into archive, false if it was dominated by a solution in the archive
	 */
	boolean addIfNotDominated(CandidateSolution solution) {
		// if the solution is dominated by anything in the archive, do nothing
		for (CandidateSolution bestSolution : mostOptimalSolutions) {
			if (bestSolution.dominates(solution))
				//solution is not added to archive, return false
				return false;
		}

		// remove any solutions from the archive that are dominated by the new solution
		mostOptimalSolutions.removeIf(solution::dominates);

		// add the new solution to the archive
		mostOptimalSolutions.add(solution);

		//a solution was added to the archive, return true
		return true;
	}

}
