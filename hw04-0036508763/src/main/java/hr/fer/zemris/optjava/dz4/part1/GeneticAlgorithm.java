package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.IFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GeneticAlgorithm {
    private static final int N_VARIABLES = 6;
    private static int popSize;
    private static double minError;
    private static int maxIter;
    private static double sigma;
    private static List<Double[]> population;
    private static Random random = new Random();
    private static double ALPHA = 0.5;
    private static IFunction function;
    private static List<Double[]> newPopulation;
    private static final int TOP_SOLUTIONS = 2;
    private static double CHANCE = 0.25;
    private static final double ORDER_OF_MAGNITUDE = 10e3;

    public static void main(String[] args) throws IOException {
        function = getFunction(getValues(Paths.get("./02-zad-prijenosna.txt")));
        popSize = Integer.parseInt(args[0]);
        minError = Double.parseDouble(args[1]);
        maxIter = Integer.parseInt(args[2]);
        sigma = Double.parseDouble(args[4]);
        population = new ArrayList<>();
        newPopulation = new ArrayList<>();
        if (args[3].equals("rouletteWheel")) {
            run(new RouletteWheelSupplier(function, population, ORDER_OF_MAGNITUDE, random));
        } else if (args[3].startsWith("tournament:")) {
            run(new TournamentSupplier(function, population, random, Integer.parseInt(args[3].split(":")[1])));
        }
    }

    private static void run(Supplier<Double[]> selectionSupplier) {
        populate();
        evaluate();
        for (int i = 0; i < maxIter; i++) {
            while (newPopulation.size() != population.size()) {
                Double[] parentX = selectionSupplier.get();
                Double[] parentY = selectionSupplier.get();
                newPopulation.add(blxAlphaCrossover(parentX, parentY));
            }
            population.clear();
            population.addAll(newPopulation);
            newPopulation.clear();
            evaluate();
            if (function.valueAt(population.get(0)) < minError) {
                break;
            }
            printSolution();
        }
        System.out.println("Final solution.");
        printSolution();
    }

    private static void printSolution() {
        System.out.print("Deviation: " + function.valueAt(population.get(0)));
        System.out.print(" Points: ");
        Arrays.stream(population.get(0)).forEach(v -> System.out.print(v + " "));
        System.out.println();
    }

    private static void evaluate() {
        for (int i = 0; i < population.size(); i++) {
            population.sort(Comparator.comparingDouble(a -> function.valueAt(a)));
        }
        for (int i = 0; i < TOP_SOLUTIONS; i++) {
            newPopulation.add(population.get(i));
        }
    }

    private static void mutate(Double[] solution, double mi, double sigma) {
        for (int j = 0; j < solution.length; j++) {
            if (random.nextDouble() < CHANCE) {
                solution[j] += solution[j] * ((random.nextGaussian() - 0.5) + mi) * sigma;
            }
        }
    }

    private static Double[] blxAlphaCrossover(Double[] parentX, Double[] parentY) {
        Double[] child = new Double[parentX.length];
        for (int i = 0; i < parentX.length; i++) {
            double cMin = Math.min(parentX[i], parentY[i]);
            double cMax = Math.max(parentX[i], parentY[i]);
            double vector = cMax - cMin;
            double value = Math.random() * (cMax + vector * ALPHA - (cMin - vector * ALPHA)) + cMin - vector * ALPHA;
            child[i] = value;
        }
        mutate(child, 0, sigma);
        return child;
    }

    private static void populate() {
        for (int i = 0; i < popSize; i++) {
            population.add(randomPoint(N_VARIABLES, new double[]{-10, 10}));
        }
    }

    private static Double[] randomPoint(int size, double[] deltas) {
        Double[] point = new Double[size];
        Random random = new Random();
        for (int i = 0; i < point.length; i++) {
            point[i] = (random.nextDouble() * (deltas[1] - deltas[0]) + deltas[0]);
        }
        return point;
    }

    private static double[][] getValues(Path path) throws IOException {
        List<String> lines;

        lines = Files.readAllLines(path);

        lines = lines.stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

        double[][] values = new double[20][6];
        int i = 0;
        for (String s : lines) {
            String[] stringValues = s.substring(1, s.length() - 2).split(", ");
            for (int j = 0; j < stringValues.length; j++) {
                values[i][j] = Double.parseDouble(stringValues[j]);
            }
            i++;
        }
        return values;
    }

    private static IFunction getFunction(double[][] values) {
        return array -> {
            double sum = 0;
            for (int j = 0; j < 20; j++) {
                double[] data = values[j];
                double currentSum = 0;
                currentSum += array[0] * data[0];
                currentSum += array[1] * pow(data[0], 3) * data[1];
                currentSum += array[2] * exp(array[3] * data[2]) * (1 + cos(array[4] * data[3]));
                currentSum += array[5] * data[3] * pow(data[4], 2);
                currentSum -= data[5];
                sum += currentSum * currentSum;
            }
            return sum;
        };
    }
}
