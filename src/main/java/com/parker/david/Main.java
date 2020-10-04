package com.parker.david;

import com.parker.david.Neighbourhood.RandomDistanceFromOriginal;
import com.parker.david.Neighbourhood.RandomDistanceFromOriginalEpochAdaptive;
import com.parker.david.Neighbourhood.SolutionFromNeighbourhoodGenerator;
import com.parker.david.epoch.EpochController;
import com.parker.david.epoch.StaticAcceptanceRejectionDependant;
import com.parker.david.initialisation.AcceptAll;
import com.parker.david.initialisation.InitialTemperatureAssignment;
import com.parker.david.search.MaxIterationsWithoutImprovement;
import com.parker.david.search.TerminationController;
import com.parker.david.temperature.GeometricSchedule;
import com.parker.david.temperature.TemperatureController;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;


public class Main {

	/**
	 * main function. Initialises a lot of variables and then runs DBMOSA with the initialisation completed
	 */
	public static void main(String[] args) throws IOException {
		//run 3 times
		FileWriter output = new FileWriter("output/output_" + ZonedDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".txt", true);
		for (int i = 0; i < 3; i++) {
			String thisRun = initialiseAndRunDbmosa() + "\n\n";
			System.out.print(thisRun);
			output.append(thisRun);
		}
		output.close();
	}

	/**
	 * set up the algorithm parameters and then run it
	 */
	private static String initialiseAndRunDbmosa() throws IOException {
		//our search parameters
		final int maxSolutionAcceptances = 6;
		final int maxSolutionRejections = 6;
		final double coolingCoefficient = 0.9;
		final double heatingCoefficient = 1.1;
		final double maxDecisionVariableVariation = 100.0;
		final double minDecisionVariableVariation = 0.1;
		final int initialisationAcceptedSolutionsNeeded = 10;
		final int iterationsWithoutImprovement = 100;
		final int maxArchiveSize = 1000;
		final double minimumTemperature = 0.0001;

		//set our constraints
		final double maxRadius = 100000;
		ConstraintSet constraints = new ConstraintSet();
		constraints.add(new Constraint(new ArrayList<>(Collections.singletonList(1)), Constraint.Operators.GREATER_THAN_OR_EQUAL, -maxRadius));
		constraints.add(new Constraint(new ArrayList<>(Collections.singletonList(1)), Constraint.Operators.LESS_THAN_OR_EQUAL, maxRadius));

		//set our objectives
		ObjectiveSet objectives = new ObjectiveSet();
		objectives.add(new ObjectiveFunction(ObjectiveFunction.Type.Minimisation) { //x0^2
			@Override
			public double getFitness(CandidateSolution solution) {
				return solution.getIthDecisionVariable(0) * solution.getIthDecisionVariable(0);
			}
		});
		objectives.add(new ObjectiveFunction(ObjectiveFunction.Type.Minimisation) { //(x0-2)^2
			@Override
			public double getFitness(CandidateSolution solution) {
				return (solution.getIthDecisionVariable(0) - 2) * (solution.getIthDecisionVariable(0) - 2);
			}
		});

		//generate our components
		CandidateSolutionFactory solutionFactory = new CandidateSolutionFactory(objectives, 2);
		EpochController epochEnd = new StaticAcceptanceRejectionDependant(maxSolutionAcceptances, maxSolutionRejections);
		TemperatureController temperatureController = new GeometricSchedule(coolingCoefficient, heatingCoefficient, minimumTemperature);
		TerminationController stoppingCriterion = new MaxIterationsWithoutImprovement(iterationsWithoutImprovement);

		//generate an initial solution
		CandidateSolution solution = solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(0.0))); // our base solution is at the origin
		solution = (new RandomDistanceFromOriginal(maxRadius, constraints)).neighbourhoodSolution(solution); // generate our first random solution, located anywhere in the feasible solution space

		//create a test neighbourhood based on the functions x^2 and (x-2)^2 with limits of |x| < 10^5
		ArrayList<CandidateSolution> extremalSolutions = new ArrayList<>();
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(100000.0)))); //the max value for x
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(0.0)))); //the zero for x^2
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(2.0)))); //the zero for (x-2)^2
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(-100000.0)))); //the min value for x

		//generate the initial temperature, note that we can't use an adaptive generation method here as there is no search meta yet (which contains the temperature)
		InitialTemperatureAssignment initialTemperatureAssignment = new AcceptAll(initialisationAcceptedSolutionsNeeded, extremalSolutions);
		double initialTemperature = initialTemperatureAssignment.getInitialTemperature(solution, objectives, new RandomDistanceFromOriginal(maxDecisionVariableVariation, constraints));

		//create an object to hold the search progress
		SearchMetaInfo searchMeta = new SearchMetaInfo(initialTemperature);
		SolutionFromNeighbourhoodGenerator nextNeighbourGenerator = new RandomDistanceFromOriginalEpochAdaptive(maxDecisionVariableVariation, minDecisionVariableVariation, constraints, searchMeta); // next neighbour generator

		//run the algorithm
		return dbmosa(searchMeta, temperatureController, solution, nextNeighbourGenerator, stoppingCriterion, epochEnd, maxArchiveSize);
	}

	/**
	 * the DBMOSA algorithm itself
	 */
	public static String dbmosa(SearchMetaInfo searchMeta, TemperatureController tempControl, CandidateSolution solution, SolutionFromNeighbourhoodGenerator neighbourhoodGenerator, TerminationController stoppingCriterion, EpochController epochEnd, int maxArchiveSize) throws IOException {

		SolutionArchive archive = new SolutionArchive(); // create an archive
		archive.addIfNotDominated(solution); // add in our initial solution

		//begin our iterations
		while (stoppingCriterion.continueSearch(searchMeta) && archive.getArchive().size() < maxArchiveSize) {

			//generate a new solution
			CandidateSolution newSolution = neighbourhoodGenerator.neighbourhoodSolution(solution);

			//check if the solution is accepted
			boolean isAccepted = AcceptanceComputer.isAccepted(archive, solution, newSolution, searchMeta.getTemperature());

			// if the solution is accepted, log an acceptance, if a new epoch is triggered:
			// set a cooler temperature, and set a new solution for the next iteration
			if (isAccepted) {
				searchMeta.solutionAcceptance(archive.addIfNotDominated(newSolution));
				if (epochEnd.newEpochTriggered(searchMeta)) {
					searchMeta.newEpoch();
					tempControl.cool(searchMeta);
				}
				solution = newSolution;
			}
			// if the solution is rejected, log a rejection, and if a new epoch is triggered:
			// set a hotter temperature, and keep this solution for the next iteration
			else {
				searchMeta.solutionRejection();
				if (epochEnd.newEpochTriggered(searchMeta)) {
					searchMeta.newEpoch();
					tempControl.heat(searchMeta);
				}
			}
		}

		//write the data out to a file
		String date = ZonedDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SS"));

		FileWriter fitnessOutput = new FileWriter("output/fitness_" + date + ".csv", true);
		archive.getArchive().forEach(sol -> sol.fitnessToFile(fitnessOutput));
		fitnessOutput.close();

		FileWriter decisionVariableOutput = new FileWriter("output/decisions_" + date + ".csv", true);
		archive.getArchive().forEach(sol -> sol.decisionVariablesToFile(decisionVariableOutput));
		decisionVariableOutput.close();

		//write the summary information to console and a file
		return "final epochs: " + searchMeta.getEpoch() + "\n" +
				"final iteration count: " + searchMeta.getIteration() + "\n" +
				"final temperature: " + searchMeta.getTemperature() + "\n" +
				"final set of (" + archive.getArchive().size() + ") solutions: " + "\n" +
				"data timestamp : " + date + "\n";
	}
}
