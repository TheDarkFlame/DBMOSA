package com.parker.david;

import com.parker.david.Neighbourhood.RandomDistanceFromOriginal;
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
		//run 10 times
		FileWriter output = new FileWriter("output/output_" + ZonedDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".txt", true);
		for (int i = 0; i < 10; i++) {
			String thisRun = initialiseAndRunDbmosa() + "\n\n";
			System.out.print(thisRun);
			output.append(thisRun);
		}
		output.close();
	}

	/**
	 * set up the algorithm parameters and then run it
	 */
	private static String initialiseAndRunDbmosa() {
		//our search parameters
		final int maxSolutionAcceptances = 6;
		final int maxSolutionRejections = 6;
		final double coolingCoefficient = 0.9;
		final double heatingCoefficient = 1.1;
		final double maxDecisionVariableVariation = 100.0;
		final int initialisationAcceptedSolutionsNeeded = 10;
		final int iterationsWithoutImprovement = 100;

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
		CandidateSolutionFactory solutionFactory = new CandidateSolutionFactory(objectives);
		EpochController epochEnd = new StaticAcceptanceRejectionDependant(maxSolutionAcceptances, maxSolutionRejections);
		TemperatureController temperatureController = new GeometricSchedule(coolingCoefficient, heatingCoefficient);
		TerminationController stoppingCriterion = new MaxIterationsWithoutImprovement(iterationsWithoutImprovement);
		SolutionFromNeighbourhoodGenerator nextNeighbourGenerator = new RandomDistanceFromOriginal(maxDecisionVariableVariation, constraints); // next neighbour generator

		//generate an initial solution
		CandidateSolution solution = solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(0.0))); // our base solution is at the origin
		solution = (new RandomDistanceFromOriginal(maxRadius, constraints)).neighbourhoodSolution(solution); // generate our first random solution, located anywhere in the feasible solution space

		//create a test neighbourhood based on the functions x^2 and (x-2)^2 with limits of |x| < 10^5
		ArrayList<CandidateSolution> extremalSolutions = new ArrayList<>();
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(100000.0)))); //the max value for x
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(0.0)))); //the zero for x^2
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(2.0)))); //the zero for (x-2)^2
		extremalSolutions.add(solutionFactory.getCandidateSolution(new ArrayList<>(Collections.singletonList(-100000.0)))); //the min value for x

		//generate the initial temperature
		InitialTemperatureAssignment initialTemperatureAssignment = new AcceptAll(initialisationAcceptedSolutionsNeeded, extremalSolutions);
		double initialTemperature = initialTemperatureAssignment.getInitialTemperature(solution, objectives, nextNeighbourGenerator);

		//run the algorithm
		return dbmosa(initialTemperature, temperatureController, solution, nextNeighbourGenerator, stoppingCriterion, epochEnd);
	}

	/**
	 * the DBMOSA algorithm itself
	 */
	public static String dbmosa(double startingTemperature, TemperatureController tempControl, CandidateSolution solution, SolutionFromNeighbourhoodGenerator neighbourhoodGenerator, TerminationController stoppingCriterion, EpochController epochEnd) {

		SolutionArchive archive = new SolutionArchive(); // create an archive
		SearchMetaInfo searchMeta = new SearchMetaInfo(startingTemperature);
		archive.addIfNotDominated(solution); // add in our initial solution

		//begin our iterations
		while (stoppingCriterion.continueSearch(searchMeta)) {

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

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("final epochs: ").append(searchMeta.getEpoch()).append("\n");
		stringBuilder.append("final iteration count: ").append(searchMeta.getIteration()).append("\n");
		stringBuilder.append("final temperature: ").append(String.format("%.6f", searchMeta.getTemperature())).append("\n");
		stringBuilder.append("final set of (").append(archive.getArchive().size()).append(") solutions: ").append("\n");
		archive.getArchive().stream().map(CandidateSolution::toString).forEach(solutionString -> stringBuilder.append(solutionString).append("\n"));
		return stringBuilder.toString();
	}
}
