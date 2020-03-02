package hr.fer.zemris.optjava.dz9;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.PlotOrientation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Program arguments
 * 1 30 decision-space 1000
 * 2 500 decision-space 100
 * 2 500 objective-space 100
 * @author Marko Ivić
 * @version 1.0.0
 */
public class MOOP {
    private final static double ALPHA = 2;
    private final static double EPSILON = 0.05;
    private final static double SIGMA_SHARE = 0.15;
    private final static double MIN = -2;
    private final static double MAX = 2;
    private final static double CHANCE = 0.1;
    private static Random random = new Random();

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Invalid number of arguments.");
            return;
        }
        MOOPProblem problem;
        if (args[0].equals("1")) {
            problem = new FirstMOOPProblem();
        } else if (args[0].equals("2")) {
            problem = new SecondMOOPProblem();
        } else {
            System.out.println("Unsupported problem");
            return;
        }
        if (!(args[2].equals("decision-space") || args[2].equals("objective-space"))) {
            System.out.println("Calculations in given space are not supported.");
            return;
        }
        run(Integer.parseInt(args[1]), Integer.parseInt(args[3]), problem, args[2]);
    }

    private static void run(int n, int maxIter, MOOPProblem problem, String space) {
        double[][] population = initPopulation(n, problem);
        double[][] solutions = new double[population.length][0];
        double[] fitness = new double[population.length];
        for (int iter = 0; iter < maxIter; iter++) {
            evaluate(population, solutions, problem);
            calculateFitness(population, solutions, fitness, space);
            double[][] newPopulation = new double[population.length][0];
            int index = 0;
            fitnessToRouletteWheelFormat(fitness);
            while (index < population.length) {
                double[] firstParent = pickParent(population, fitness);
                double[] secondParent;
                do {
                    secondParent = pickParent(population, fitness);
                } while (firstParent == secondParent);
                double[] child = crossover(firstParent, secondParent);
                mutate(child);
                problem.limit(child);
                newPopulation[index] = child;
                index++;
            }
            population = newPopulation;
        }
        plot(problem instanceof FirstMOOPProblem ? "first" : "second", solutions, space);
        output(solutions, population);
    }

    private static void output(double[][] solutions, double[][] population) {
        int[][] fronts = nonDominantSort(solutions);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fronts.length; i++) {
            sb.append((i + 1) + ". front has: " + fronts[i].length + " solutions").append("\n");
        }
        System.out.println(sb);
        StringBuilder decision = new StringBuilder();
        StringBuilder objective = new StringBuilder();
        for (int i = 0; i < population.length; i++) {
            decision.append("Decision: ");
            for (int j = 0; j < population[i].length; j++) {
                decision.append(population[i][j] + ", ");
            }
            decision.deleteCharAt(decision.length() - 1).deleteCharAt(decision.length() - 1).append("\n");
            objective.append("Objective: ");
            for (int j = 0; j < solutions[i].length; j++) {
                objective.append(solutions[i][j] + ", ");
            }
            objective.deleteCharAt(objective.length() - 1).deleteCharAt(objective.length() - 1).append("\n");
        }
        try {
            Files.write(Paths.get("./izlaz-dec.txt"), decision.toString().getBytes());
            Files.write(Paths.get("./izlaz-obj.txt"), objective.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Unable to write to txt.");
        }
    }

    private static void plot(String name, double[][] solution, String space) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Points");
        for (int i = 0; i < solution.length; i++) {
            series.add(solution[i][0], solution[i][1]);
        }
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createScatterPlot("Paret", "x", "y", dataset, PlotOrientation.VERTICAL, false, false, false);
        try {
            ChartUtilities.saveChartAsPNG(Paths.get("./" + name + ":" + space + ".png").toFile(), chart, 800, 800);
        } catch (IOException e) {
            System.out.println("Unable to save image.");
        }
    }

    private static void mutate(double[] child) {
        for (int i = 0; i < child.length; i++) {
            if (random.nextDouble() < CHANCE) {
                child[i] += random.nextDouble() * 0.5 - 0.25;
            }
        }
    }

    private static double[] crossover(double[] firstParent, double[] secondParent) {
        double[] child = new double[firstParent.length];
        for (int i = 0; i < firstParent.length; i++) {
            child[i] = random.nextBoolean() ? firstParent[i] : secondParent[i];
        }
        return child;
    }

    private static double[] pickParent(double[][] population, double[] fitness) {
        double randomNumber = random.nextDouble();
        int selectedParent = -1;
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] >= randomNumber) {
                selectedParent = i;
                break;
            }
        }
        return population[selectedParent];
    }

    private static void fitnessToRouletteWheelFormat(double[] fitness) {
        double sum = 0;
        for (int i = 0; i < fitness.length; i++) {
            sum += fitness[i];
        }

        // norm chances
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] /= sum;
            if (i > 0) {
                fitness[i] += fitness[i - 1];
            }
        }
    }

    private static void evaluate(double[][] population, double[][] solutions, MOOPProblem problem) {
        for (int i = 0; i < population.length; i++) {
            double[] solution = new double[problem.getNumberOfObjectives()];
            problem.evaluateSolution(population[i], solution);
            solutions[i] = solution;
        }
    }

    private static void calculateFitness(double[][] population, double[][] solutions, double[] fitness, String space) {
        int j = 0;
        int[][] fronts = nonDominantSort(solutions);
        double fitnessMin = population.length + EPSILON;
        while (j < fronts.length) {
            double currentlyLowestFitness = fitnessMin;
            for (int i = 0; i < fronts[j].length; i++) {
                fitness[fronts[j][i]] = fitnessMin - EPSILON;
                double nc;
                if (space.equals("decision-space")) {
                    nc = calculateNicheDensity(population, i);
                } else {
                    nc = calculateNicheDensity(solutions, i);
                }
                fitness[fronts[j][i]] /= nc;
                currentlyLowestFitness = Math.min(fitness[fronts[j][i]], currentlyLowestFitness);
            }
            fitnessMin = currentlyLowestFitness;
            j++;
        }
    }

    private static double calculateNicheDensity(double[][] population, int i) {
        double nicheDensity = 0;
        for (int j = 0; j < population.length; j++) {
            nicheDensity += calculateShare(calculateDistance(population[i], population[j]));
        }
        return nicheDensity;
    }

    private static double calculateDistance(double[] firstPoint, double[] secondPoint) {
        double distance = 0;
        for (int i = 0; i < firstPoint.length; i++) {
            distance += Math.pow((firstPoint[i] - secondPoint[i]) / (MAX - MIN), 2);
        }
        return Math.sqrt(distance);
    }

    private static double calculateShare(double distance) {
        if (distance > SIGMA_SHARE) {
            return 0;
        }
        return 1 - Math.pow(distance / SIGMA_SHARE, ALPHA);
    }


    private static double[][] initPopulation(int n, MOOPProblem problem) {
        double[][] population = new double[n][problem.getNumberOfObjectives()];
        for (int i = 0; i < population.length; i++) {
            population[i] = problem.generate(random);
        }
        return population;
    }

    private static int[][] nonDominantSort(double[][] solutions) {
        int[] dominatedBy = new int[solutions.length];
        List<List<Integer>> dominates = new ArrayList<>();
        for (int i = 0; i < solutions.length; i++) {
            dominates.add(new ArrayList<>());
        }
        List<List<Integer>> fronts = new ArrayList<>();
        fronts.add(new ArrayList<>());
        for (int i = 0; i < solutions.length; i++) {
            fronts.get(0).add(i);
            for (int j = 0; j < solutions.length; j++) {
                if (i == j) {
                    continue;
                }
                if (dominates(solutions[i], solutions[j])) {
                    dominates.get(i).add(j);
                } else if (dominates(solutions[j], solutions[i])) {
                    dominatedBy[i]++;
                }
            }
        }
        List<Integer> bestFront = new ArrayList<>();
        for (int i = fronts.get(0).size() - 1; i >= 0; i--) {
            if (dominatedBy[i] == 0) {
                bestFront.add(fronts.get(0).get(i));
            }
        }
        fronts.clear();
        fronts.add(bestFront);
        int k = 0;
        do {
            List<Integer> front = new ArrayList<>();
            for (int i = 0; i < fronts.get(k).size(); i++) {
                while (dominates.get(fronts.get(k).get(i)).size() > 0) {
                    dominatedBy[dominates.get(fronts.get(k).get(i)).get(0)]--;
                    if (dominatedBy[dominates.get(fronts.get(k).get(i)).get(0)] == 0) {
                        front.add(dominates.get(fronts.get(k).get(i)).get(0));
                    }
                    dominates.get(fronts.get(k).get(i)).remove(dominates.get(fronts.get(k).get(i)).get(0));
                }
            }
            fronts.add(front);
            k++;
        } while (fronts.get(k).size() != 0);

        fronts.remove(fronts.size() - 1);

        int[][] frontsArray = new int[fronts.size()][0];
        for (int i = 0; i < fronts.size(); i++) {
            frontsArray[i] = new int[fronts.get(i).size()];
            for (int j = 0; j < fronts.get(i).size(); j++) {
                frontsArray[i][j] = fronts.get(i).get(j);
            }
        }
        return frontsArray;
    }

    private static boolean dominates(double[] firstSolution, double[] secondSolution) {
        boolean better = false;
        for (int i = 0; i < firstSolution.length; i++) {
            if (firstSolution[i] < secondSolution[i]) {
                better = true;
            } else if (firstSolution[i] > secondSolution[i]) {
                return false;
            }
        }
        return better;
    }
}
