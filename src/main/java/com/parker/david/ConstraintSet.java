package com.parker.david;

import java.util.ArrayList;

/**
 * a holder for all constraints so it is simpler to check if all constraints are feasible at once
 */
public class ConstraintSet {

	/**
	 * this is an array of the constraints that we want to check
	 */
	private ArrayList<Constraint> constraintSet;

	/**
	 * in order to feel similar to the ArrayList.add method, this accepts a new constraint.
	 * it adds it to the internal constraint set
	 *
	 * @param constraint this is the constraint that we want to add in to the existing set of constraints
	 * @return returns the constraint set object so that the .add() function can be chained multiple times.
	 */
	public ConstraintSet add(Constraint constraint) {
		constraintSet.add(constraint);
		return this;
	}

	/**
	 * constructor, initialises the internal arraylist
	 */
	ConstraintSet() {
		constraintSet = new ArrayList<>();
	}

	/**
	 * this loops through all constraints that are present, and returns false if one of them is violated
	 *
	 * @param solution this is the solution that we want to check feasibility for
	 * @return a boolean, if true solution is feasible, if false, solution is unfeasible
	 */
	public boolean isFeasible(CandidateSolution solution) {
		for (Constraint constraint : constraintSet) {
			if (!constraint.isFeasible(solution))
				return false;
		}
		return true;
	}
}
