package hr.fer.zemris.optjava.dz10;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class MOOP {
    private final static double MIN = -1;
    private final static double MAX = 1;
    private final static double CHANCE = 0.1;
    private static Random random = new Random();

    public static void main(String[] args) {
        if (args.length != 3) {
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
        run(Integer.parseInt(args[1]), Integer.parseInt(args[2]), problem);
    }

    private static void run(int n, int maxIter, MOOPProblem problem) {
        double[][] population = initPopulation(n, problem);
        double[][] solutions = new double[population.length * 2][0];
        double[][] crowdingDistance;
        int dominatedBy[] = new int[population.length * 2];
        int[][] fronts;
        double[][] children = initPopulation(n, problem);
        for (int iter = 0; iter < maxIter; iter++) {
            double[][] wholePopulation = createWholePopulation(population, children);
            evaluate(wholePopulation, solutions, problem);
            double[][] newPopulation = new double[population.length][];
            fronts = nonDominantSort(solutions, dominatedBy);
            crowdingDistance = calculateCrowdingDistance(solutions, fronts);
            int filled = 0;
            int miFront = 0;
            for (int i = 0; i < fronts.length; i++) {
                miFront = i;
                if (filled + fronts[i].length <= newPopulation.length) {
                    for (int j = 0; j < fronts[i].length; j++) {
                        newPopulation[filled + j] = wholePopulation[fronts[i][j]];
                    }
                    filled += fronts[i].length;
                } else {
                    break;
                }
            }
            double[][] lastFrontPopulation = new double[fronts[miFront].length][];
            for (int i = 0; i < fronts[miFront].length; i++) {
                lastFrontPopulation[i] = wholePopulation[fronts[miFront][i]];
            }
            Integer[] indexes = getSortedIndexes(lastFrontPopulation, crowdingDistance[miFront], true);
            for (int i = filled; i < newPopulation.length; i++) {
                newPopulation[i] = lastFrontPopulation[indexes[i - filled]];
            }
            population = newPopulation;
            int[] newPopulationDominatedBy = new int[population.length];
            double[][] newPopulationSolutions = new double[population.length][0];
            evaluate(newPopulation, newPopulationSolutions, problem);
            fronts = nonDominantSort(newPopulationSolutions, newPopulationDominatedBy);
            crowdingDistance = calculateCrowdingDistance(solutions, fronts);
            double[] newPopulationCrowdingDistance = new double[population.length];
            int index = 0;
            for (int i = 0; i < crowdingDistance.length; i++) {
                for (int j = 0; j < crowdingDistance[i].length; j++) {
                    newPopulationCrowdingDistance[index++] = crowdingDistance[i][j];
                }
            }
            children = createChildren(population, newPopulationDominatedBy, newPopulationCrowdingDistance, problem);
        }
        double[][] finalSolutions = new double[population.length][];
        evaluate(population, finalSolutions, problem);
        plot(problem instanceof FirstMOOPProblem ? "first" : "second", finalSolutions);
        output(finalSolutions, population);
    }

    private static double[][] createWholePopulation(double[][] population, double[][] children) {
        double[][] wholePopulation = new double[population.length + children.length][];
        for (int i = 0; i < population.length; i++) {
            wholePopulation[i] = population[i];
        }
        for (int i = population.length; i < population.length + children.length; i++) {
            wholePopulation[i] = children[i - children.length];
        }
        return wholePopulation;
    }

    private static class DistanceComparator implements Comparator<Integer> {
        private double[] solutions;

        private DistanceComparator(double[] solutions) {
            this.solutions = solutions;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            return Double.compare(solutions[o1], solutions[o2]);
        }
    }


    private static double[][] calculateCrowdingDistance(double[][] solutions, int[][] fronts) {
        double[][] crowdingDistance = new double[fronts.length][0];
        for (int i = 0; i < fronts.length; i++) {
            double[][] solutionsInThisFront = new double[fronts[i].length][];
            for (int j = 0; j < fronts[i].length; j++) {
                solutionsInThisFront[j] = solutions[fronts[i][j]];
            }
            crowdingDistance[i] = new double[solutionsInThisFront.length];
            for (int j = 0; j < solutionsInThisFront[0].length; j++) {
                double[] goalFunction = extractGoalFunction(solutionsInThisFront, j);
                Integer[] indexes = getSortedIndexes(solutionsInThisFront, goalFunction, false);
                calculateDistance(crowdingDistance, i, indexes, goalFunction);
            }
        }
        return crowdingDistance;
    }

    private static Integer[] getSortedIndexes(double[][] solutions, double[] goalFunction, boolean reversed) {
        DistanceComparator distanceComparator = new DistanceComparator(goalFunction);
        Integer[] indexes = generateIndexes(solutions.length);
        if (reversed) {
            Arrays.sort(indexes, distanceComparator.reversed());
        } else {
            Arrays.sort(indexes, distanceComparator);
        }
        return indexes;
    }

    private static void calculateDistance(double[][] crowdingDistance, int front, Integer[] indexes, double[] solutions) {
        crowdingDistance[front][indexes[0]] = Double.POSITIVE_INFINITY;
        crowdingDistance[front][indexes[indexes.length - 1]] = Double.POSITIVE_INFINITY;
        for (int i = 1; i < indexes.length - 1; i++) {
            crowdingDistance[front][indexes[i]] += (solutions[indexes[i - 1]] - solutions[indexes[i + 1]]) /
                    (solutions[indexes[0]] - solutions[indexes[indexes.length - 1]]);
        }
    }

    private static Integer[] generateIndexes(int length) {
        Integer[] indexes = new Integer[length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        return indexes;
    }

    private static double[] extractGoalFunction(double[][] solutions, int index) {
        double[] goalFunctions = new double[solutions.length];
        for (int i = 0; i < solutions.length; i++) {
            goalFunctions[i] = solutions[i][index];
        }
        return goalFunctions;
    }

    private static double[][] createChildren(double[][] population, int[] dominatedBy, double[] crowdingDistance, MOOPProblem problem) {
        double[][] children = new double[population.length][];
        int numberOfChildren = 0;
        while (numberOfChildren < children.length) {
            double[] firstParent = pickParent(population, dominatedBy, crowdingDistance);
            double[] secondParent;
            do {
                secondParent = pickParent(population, dominatedBy, crowdingDistance);
            } while (firstParent == secondParent);
            double[] child = crossover(firstParent, secondParent);
            mutate(child);
            problem.limit(child);
            children[numberOfChildren] = child;
            numberOfChildren++;
        }
        return children;
    }

    private static double[] pickParent(double[][] population, int[] dominatedBy, double[] crowdingDistance) {
        int firstIndex = random.nextInt(population.length);
        int secondIndex;
        do {
            secondIndex = random.nextInt(population.length);
        } while (firstIndex == secondIndex);
        if (dominatedBy[firstIndex] < dominatedBy[secondIndex]) {
            return population[firstIndex];
        } else if (dominatedBy[firstIndex] > dominatedBy[secondIndex]) {
            return population[secondIndex];
        }
        if (crowdingDistance[firstIndex] > crowdingDistance[secondIndex]) {
            return population[firstIndex];
        } else {
            return population[secondIndex];
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

    private static void evaluate(double[][] population, double[][] solutions, MOOPProblem problem) {
        for (int i = 0; i < population.length; i++) {
            double[] solution = new double[problem.getNumberOfObjectives()];
            problem.evaluateSolution(population[i], solution);
            solutions[i] = solution;
        }
    }

    private static double[][] initPopulation(int n, MOOPProblem problem) {
        double[][] population = new double[n][problem.getNumberOfObjectives()];
        for (int i = 0; i < population.length; i++) {
            population[i] = problem.generate(random);
        }
        return population;
    }

    private static int[][] nonDominantSort(double[][] solutions, int[] returningDominatedBy) {
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
        System.arraycopy(dominatedBy, 0, returningDominatedBy, 0, dominatedBy.length);
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

    private static void output(double[][] solutions, double[][] population) {
        int[][] fronts = nonDominantSort(solutions, new int[solutions.length]);
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

    private static void plot(String name, double[][] solution) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Points");
        for (int i = 0; i < solution.length; i++) {
            series.add(solution[i][0], solution[i][1]);
        }
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createScatterPlot("Paret", "x", "y", dataset, PlotOrientation.VERTICAL, false, false, false);

        XYPlot xyPlot = chart.getXYPlot();
        ValueAxis domainAxis = xyPlot.getDomainAxis();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();

        domainAxis.setRange(0.1, 1.0);
        rangeAxis.setRange(0.0, 10);

        try {
            ChartUtilities.saveChartAsPNG(Paths.get("./" + name  + ".png").toFile(), chart, 800, 800);
        } catch (IOException e) {
            System.out.println("Unable to save image.");
        }
    }
}
