package com.parker.david;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This is our data class that stores a candidate solution.
 * It is mostly just a wrapper for arraylist, but also contains a method that makes a deepcopy of itself
 * (thus creating a new entity that is the same as this entity with underlying data
 * that is a copy of the previous object instead of a reference to the same underlying data)
 */
public class CandidateSolution implements Comparable<CandidateSolution> {

	/**
	 * the internal id of this solution
	 */
	private final int solutionId;

	/**
	 * the factory that created this object
	 */
	private final CandidateSolutionFactory factory;

	/**
	 * our decision variables encoded as a Double vector
	 */
	private final ArrayList<Double> decisionVariables;

	/**
	 * the number of decision variables in this problem, needed for iterating through the candidate solution
	 *
	 * @return the number of decision variables
	 */
	public int numberOfDecisionVariables() {
		return decisionVariables.size();
	}

	/**
	 * get the ith decision variable
	 * given: [5,3,2,7,4,0,6]
	 * getIthDecisionVariable(0) = 5, 1 = 3, etc
	 *
	 * @param i the ith decision variable's value
	 * @return a Double corresponding to the decision variable at the ith position
	 */
	public Double getIthDecisionVariable(int i) {
		return decisionVariables.get(i);
	}

	/**
	 * same as the getter, but sets the decision variable instead
	 *
	 * @param i        the ith decision variable's value
	 * @param newValue the value to set the decision variable to
	 */
	public void setIthDecisionVariable(int i, Double newValue) {
		decisionVariables.set(i, newValue);
	}

	/**
	 * our constructor, take an arraylist of Doubles and wraps it to create com.parker.david.CandidateSolution
	 *
	 * @param decisionVariables an arraylist of Doubles correlating to the decision variables
	 */
	CandidateSolution(ArrayList<Double> decisionVariables, int solutionId, CandidateSolutionFactory factoryRef) {
		this.decisionVariables = decisionVariables;
		this.solutionId = solutionId;
		this.factory = factoryRef;
	}

	/**
	 * our deep copy method. Creates a brand new CandidateSolution that is a copy of this CandidateSolution.
	 * Changing data in one object does not affect data in the other object.
	 *
	 * @return a new solution that is a copy of the current one
	 */
	CandidateSolution copy() {
		ArrayList<Double> copyOfDecisionVariables = new ArrayList<>();
		for (Double decisionVariable : decisionVariables) {
			copyOfDecisionVariables.add(decisionVariable);
		}

		//get a new solution (and all the things that come with making it from the factory)
		return factory.getCandidateSolution(copyOfDecisionVariables);
	}

	/**
	 * toString method formats out Candidate solution nicely as an array of 0 and 1.
	 * example output: [9.4,7.2]
	 *
	 * @return a string representation of the decision variables encoded in 1s and 0s
	 */
	@Override
	public String toString() {
		return "decision variables: [" + decisionVariables.stream().map(aDouble -> String.format("%.6f", aDouble)).collect(Collectors.joining(", ")) +
				"], fitnesses: [" + getFitnesses().stream().map(aDouble -> String.format("%.6f", aDouble)).collect(Collectors.joining(", ")) + "]";
	}

	/**
	 * an accessor to compare two solutions, returns true if this solution dominates another solution
	 *
	 * @param otherSolution the solution we want to check if this solution dominates
	 * @return boolean, true if this dominates other
	 */
	public boolean dominates(CandidateSolution otherSolution) {
		return (compareTo(otherSolution) == 1);
	}

	/**
	 * calculate if this solution dominates another solution, 1 if dominates, 0 if not, -1 if dominated
	 */
	@Override
	public int compareTo(CandidateSolution other) {

		//positive delta indicates that for this objective function, >this< is better than >other<

		boolean positiveDeltaExists = false;
		boolean negativeDeltaExists = false;
		for (double fitnessDelta : factory.objectives.fitnessDeltasRelatedToMinMax(this, other)) {
			if (fitnessDelta > 0) positiveDeltaExists = true;
			if (fitnessDelta < 0) negativeDeltaExists = true;
		}

		if (positiveDeltaExists && !negativeDeltaExists)
			return 1; //this dominates other -> fitness deltas are all positive, and at least one is non-zero
		else if (!positiveDeltaExists && negativeDeltaExists)
			return -1; //other dominates this -> fitness deltas are all negative, and at least one is non-zero
		else
			return 0; //neither dominates -> fitness deltas are a mix of positive and negative or deltas all are zero
	}

	/**
	 * getter for fitness set of this solution
	 */
	public ArrayList<Double> getFitnesses() {
		return factory.objectives.getFitnesses(this);
	}
}