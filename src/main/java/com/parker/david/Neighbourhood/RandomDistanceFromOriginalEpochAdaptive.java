package com.parker.david.Neighbourhood;

import com.parker.david.CandidateSolution;
import com.parker.david.ConstraintSet;
import com.parker.david.SearchMetaInfo;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * calculates a defined size neighbourhood of solutions around the source, with random distance from the source
 * the size of the max change is related to the epoch number
 */
public class RandomDistanceFromOriginalEpochAdaptive implements SolutionFromNeighbourhoodGenerator {

	/**
	 * a reference to the search meta data to allow for adaptive methods
	 */
	SearchMetaInfo searchMetaInfo;

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
	 * the min any decision variable may be changed, independent of any other decision variable
	 * this prevents the change from being too small, causing basically identical solutions to be generated, resulting in very long run times
	 */
	double minDecisionVariableChange;

	/**
	 * constructor
	 *
	 * @param constraints               the set of constraints which bound the optimisation problem
	 * @param searchMetaInfo            the current search progress
	 * @param maxDecisionVariableChange the max change permitted for any decision variable
	 * @param minDecisionVariableChange the min change permitted for any decision variable
	 */
	public RandomDistanceFromOriginalEpochAdaptive(double maxDecisionVariableChange, double minDecisionVariableChange, ConstraintSet constraints, SearchMetaInfo searchMetaInfo) {
		this.maxDecisionVariableChange = maxDecisionVariableChange;
		this.minDecisionVariableChange = minDecisionVariableChange;
		this.constraints = constraints;
		this.searchMetaInfo = searchMetaInfo;
	}

	/**
	 * generate the neighbourhood around the supplied solution, each decision variable may be adjusted by at most,
	 * the the maxDecisionVariableChange in the positive or negative direction. generates neighbourhoodSize number of
	 * new candidate solutions.
	 * solutions are given max range of  max * max/(max+epoch-1)      note epoch starts at 1
	 *
	 * @param originalSolution the original solution around which a new solution is being created
	 * @return an array list of candidate solutions around the original solution
	 */
	@Override
	public CandidateSolution neighbourhoodSolution(CandidateSolution originalSolution) {

		//variation is adaptive, max(minChange,max*max(epoch+max))
		double variation = Math.max(
				maxDecisionVariableChange * maxDecisionVariableChange / (maxDecisionVariableChange + searchMetaInfo.getEpoch() - 1)
				, minDecisionVariableChange);

		//loop to try find a feasible solution
		for (int infeasibleCounter = 0; infeasibleCounter < 10; infeasibleCounter++) {
			//generate a copy of the solution that doesn't affect the underlying data of the old solution
			CandidateSolution solution = originalSolution.copy();
			for (int i = 0; i < solution.numberOfDecisionVariables(); i++) {
				//get a random number between -1 and 1, and multiply by the max change allowed to get our adjustment for this decision var
				double adjustment = ((0.5 - randomNumberGenerator.nextDouble()) * 2) * variation;
				solution.setIthDecisionVariable(i, solution.getIthDecisionVariable(i) + adjustment);
			}
			if (constraints.isFeasible(solution))
				return solution;
		}

		//if we generate too many infeasible solutions, return null
		return null;
	}
}
