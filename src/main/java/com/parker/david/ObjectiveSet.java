package com.parker.david;

import java.util.ArrayList;

/**
 * a set of all objective functions created
 */
public class ObjectiveSet {
	/**
	 * internal collection of objective functions
	 */
	private ArrayList<ObjectiveFunction> objectiveFunctions = new ArrayList<>();

	/**
	 * return fitness array list
	 */
	public ArrayList<Double> getFitnesses(CandidateSolution solution) {
		ArrayList<Double> fitnesses = new ArrayList<>();
		for (ObjectiveFunction f : objectiveFunctions) {
			fitnesses.add(f.getFitness(solution));
		}
		return fitnesses;
	}

	/**
	 * add in a new objective function which we want to optimise
	 *
	 * @param objective the objective function which we want to maximise for
	 */
	public void add(ObjectiveFunction objective) {
		objectiveFunctions.add(objective);
	}

	/**
	 * calculates the difference in fitness for each objective function when applied to baseSolution and comparisonSolution
	 * if baseSolution is better, the value is positive. if comparisonSolution is better, the value is negative
	 *
	 * @param baseSolution       the solution from which we are doing the comparison
	 * @param comparisonSolution the solution that we are comparing baseSolution to
	 */
	public ArrayList<Double> fitnessDeltasRelatedToMinMax(CandidateSolution baseSolution, CandidateSolution comparisonSolution) {
		ArrayList<Double> deltas = new ArrayList<>();
		for (int i = 0; i < objectiveFunctions.size(); i++) {
			if (objectiveFunctions.get(i).functionType == ObjectiveFunction.Type.Minimisation) {
				//if minimisation, we seek baseSolution smaller than comparisionSolution for positive instance
				deltas.add(comparisonSolution.getFitnesses().get(i) - baseSolution.getFitnesses().get(i));
			} else {
				//if maximisation, we seek baseSolution larger than comparisionSolution for positive instance
				deltas.add(baseSolution.getFitnesses().get(i) - comparisonSolution.getFitnesses().get(i));
			}
		}
		return deltas;
	}
}
