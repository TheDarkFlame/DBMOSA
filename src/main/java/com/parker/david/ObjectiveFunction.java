package com.parker.david;

public abstract class ObjectiveFunction {

	/**
	 * define if this objective function needs to be maximised or minimised
	 */
	enum Type {
		Maximisation,
		Minimisation
	}

	/**
	 * the function type of this particular objective function
	 */
	Type functionType;

	/**
	 * constructor
	 * <p>
	 * requires defining whether this is maximisation or minimisation
	 */
	ObjectiveFunction(Type functionType) {
		this.functionType = functionType;
	}

	/**
	 * get fitness for the decision variables defined by this objective function
	 * abstract function, so implementation must be defined upon creation, offering maximum flexibility in defining an objective function
	 *
	 * @param solution the solution for which the fitness is to be calculated
	 */
	abstract public double getFitness(CandidateSolution solution);
}
