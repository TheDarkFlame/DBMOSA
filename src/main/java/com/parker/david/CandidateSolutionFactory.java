package com.parker.david;

import java.util.ArrayList;

/**
 * a factory for generating new candidate solutions, keeps track of the objective functions and the id of the solution
 */
public class CandidateSolutionFactory {

	private int solutionCounter = 0;
	ObjectiveSet objectives;

	public CandidateSolution getCandidateSolution(ArrayList<Double> decisionVariables) {
		return new CandidateSolution(decisionVariables, solutionCounter++, this);
	}

	CandidateSolutionFactory(ObjectiveSet objectives) {
		this.objectives = objectives;
	}

}
