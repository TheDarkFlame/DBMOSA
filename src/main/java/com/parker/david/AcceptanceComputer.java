package com.parker.david;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AcceptanceComputer {
	private static final Random randomNumberGenerator = ThreadLocalRandom.current();

	/**
	 * calculate the energy difference between new solution and old solution
	 *
	 * @param archive     the current archive
	 * @param oldSolution the solution we are making the movement from
	 * @param newSolution the solution we are moving towards
	 */
	private static double getEnergyDifference(SolutionArchive archive, CandidateSolution oldSolution, CandidateSolution newSolution) {

		ArrayList<CandidateSolution> allSolutionsConsidered = (ArrayList<CandidateSolution>) archive.getArchive().clone();
		allSolutionsConsidered.add(oldSolution);
		allSolutionsConsidered.add(newSolution);
		double energy = 0;
		//since energy difference is ((#slns dominated by old sln) - (#slns dominated by new sln))/(archive+2)
		//just increment when old sln dominates, and decrement when new sln dominates
		for (CandidateSolution solution : allSolutionsConsidered) {
			if (solution.dominates(oldSolution)) energy++;
			if (solution.dominates(newSolution)) energy--;
		}
		return energy / allSolutionsConsidered.size();
	}

	/**
	 * boolean check to see if a solution is accepted or rejected
	 *
	 * @param archive     the archive
	 * @param oldSolution the solution moving from
	 * @param newSolution the solution we are moving to
	 * @param temperature the current temperature
	 */
	public static boolean isAccepted(SolutionArchive archive, CandidateSolution oldSolution, CandidateSolution newSolution, double temperature) {
		// calculate acceptance probability
		double deltaE = AcceptanceComputer.getEnergyDifference(archive, newSolution, oldSolution);
		double acceptanceProbability = Math.min(1, Math.exp(-deltaE / temperature));

		// stochastically generate our rejection threshold between 0 and 1
		double rejectionThreshold = randomNumberGenerator.nextDouble();

		//return true if accepted, false if rejected
		return !(rejectionThreshold > acceptanceProbability);

	}
}
