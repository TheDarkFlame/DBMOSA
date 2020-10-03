package com.parker.david;

import java.util.ArrayList;

/**
 * this class represents a single constraint. It is of the form [list of weights] [operator] [value] eg: (-1 * X1) + (3 * X2) <= 7
 * it has a method to initialise it, and a method to check if the constraint is satisfied for a candidate solution
 */
public class Constraint {

	/**
	 * an array list that contains the weights(if any) correlating to the various constraints
	 */
	private final ArrayList<Integer> constraintFunctionEncoded;

	/**
	 * an operator enum, this defines the comparison method used for evaluating feasibility
	 */
	private final Operators operator;

	/**
	 * the value that we are comparing to
	 */
	private final double valueThreshold;

	/**
	 * these are the operators that are permissible
	 * an example is:
	 * x1 <= 15
	 * would be LESS_THAN_OR_EQUAL
	 */
	public enum Operators {
		LESS_THAN,
		GREATER_THAN,
		LESS_THAN_OR_EQUAL,
		GREATER_THAN_OR_EQUAL
	}

	/**
	 * our constructor, we initialise all parameters that define a constraint, the weights, the operator, and the value
	 *
	 * @param constraintFunctionEncoded this is the set of weights that are multiplied by the vector encoded values
	 * @param operator                  this is the operator to be used
	 * @param valueThreshold            this is the value threshold that the operator applies to
	 */
	Constraint(ArrayList<Integer> constraintFunctionEncoded, Operators operator, double valueThreshold) {
		this.constraintFunctionEncoded = constraintFunctionEncoded;
		this.operator = operator;
		this.valueThreshold = valueThreshold;
	}

	/**
	 * very similar to a fitness function, except returns a boolean result. Either the function is feasible or it is not.
	 *
	 * @param solution this is the candidate solution that we seek to evaluate feasibility for
	 * @return a boolean indicating if this particular constraint is satisfied
	 */
	public boolean isFeasible(CandidateSolution solution) {
		//set an initial value
		int solutionConstraintValue = 0;

		//add up all the (weight*value)s that are used by the candidate solution
		for (int i = 0; i < constraintFunctionEncoded.size(); i++) {
			solutionConstraintValue += constraintFunctionEncoded.get(i) * solution.getIthDecisionVariable(i);
		}

		//handle the various possible operators
		if (operator == Operators.LESS_THAN)
			return solutionConstraintValue < valueThreshold;
		else if (operator == Operators.LESS_THAN_OR_EQUAL)
			return solutionConstraintValue <= valueThreshold;
		else if (operator == Operators.GREATER_THAN)
			return solutionConstraintValue > valueThreshold;
		else if (operator == Operators.GREATER_THAN_OR_EQUAL)
			return solutionConstraintValue >= valueThreshold;
		else throw new ExceptionInInitializerError("no operator selected, this should never happen");
	}
}

