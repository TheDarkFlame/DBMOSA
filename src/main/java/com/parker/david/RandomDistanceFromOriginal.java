package com.parker.david;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * calculates a defined size neighbourhood of solutions around the source, with random distance from the source
 */
public class RandomDistanceFromOriginal implements SolutionFromNeighbourhoodGenerator {

	/**
	 * the constraints to allow generation only of feasible solutions
	 */
	ConstraintSet constraints;

	/**
	 * a random number generator
	 */
	private final Random randomNumberGenerator = ThreadLocalRandom.current();

	/**
	 * the max any decision variable may be changed, independent of any other decision variable
	 */
	double maxDecisionVariableChange;

	/**
	 * constructor
	 *
	 * @param maxDecisionVariableChange the max change permitted for any decision variable
	 */
	RandomDistanceFromOriginal(double maxDecisionVariableChange, ConstraintSet constraints) {
		this.maxDecisionVariableChange = maxDecisionVariableChange;
		this.constraints = constraints;
	}

	/**
	 * generate the neighbourhood around the supplied solution, each decision variable may be adjusted by at most,
	 * the the maxDecisionVariableChange in the positive or negative direction. generates neighbourhoodSize number of
	 * new candidate solutions
	 *
	 * @param originalSolution the original solution around which a new solution is being created
	 * @return an array list of candidate solutions around the original solution
	 */
	@Override
	public CandidateSolution neighbourhoodSolution(CandidateSolution originalSolution) {

		//loop to try find a feasible solution
		for (int infeasibleCounter = 0; infeasibleCounter < 10; infeasibleCounter++) {
			//generate a copy of the solution that doesn't affect the underlying data of the old solution
			CandidateSolution solution = originalSolution.copy();
			for (int i = 0; i < solution.numberOfDecisionVariables(); i++) {
				//get a random number between -1 and 1, and multiply by the max change allowed to get our adjustment for this decision var
				double adjustment = (0.5 - randomNumberGenerator.nextDouble()) * 2 * maxDecisionVariableChange;
				solution.setIthDecisionVariable(i, solution.getIthDecisionVariable(i) + adjustment);
			}
			if (constraints.isFeasible(solution))
				return solution;
		}

		//if we generate too many infeasible solutions, return null
		return null;
	}
}
