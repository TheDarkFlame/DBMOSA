package com.parker.david;

public abstract class ObjectiveFunction {

	enum Type {
		Maximisation,
		Minimisation
	}

	Type functionType;

	ObjectiveFunction(Type functionType) {
		this.functionType = functionType;
	}

	/**
	 * get fitness for the decision variables defined by this objective function
	 */
	abstract public double getFitness(CandidateSolution solution);
}
