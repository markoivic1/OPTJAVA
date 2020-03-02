package hr.fer.zemris.optjava.dz5.part1;

import java.util.*;
import java.util.function.Function;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GeneticAlgorithm {
    private static final double MUTATION_CHANCE = 0.01;
    private static int n;
    private static int K = 2;
    private static final int maxIter = 1000;
    private static IFunction function = getFunction();
    private static Random random = new Random();
    private static int MAX_POP = 100;
    private static int MIN_POP = 20;
    private static int EFFORT = 2 * MAX_POP;
    private static double ACT_SEL_PRESS = 1;
    private static double MAX_SEL_PRESS = 5;
    private static Function<Boolean[], Double> compFactor = getCompFactor();
    private static Set<Boolean[]> population;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments");
            return;
        }
        n = Integer.parseInt(args[0]);
        initPopulation();
        run("tournament");
        Boolean[] best = kTournament(population, population.size());
        printVector(best);
        System.out.println(function.valueAt(best));
    }

    private static void printVector(Boolean[] best) {
        for (Boolean bit : best) {
            System.out.print(bit ? 1 : 0);
        }
        System.out.println();
    }

    private static void run(String selection) {
        int childrenCounter = 0;
        int oldPopSize = population.size();
        for (int j = 0; j < maxIter; j++) {
            Set<Boolean[]> newPopulation = new HashSet<>();
            for (int i = 0; i < EFFORT; i++) {
                Boolean[] firstParent = kTournament(population, K);
                Boolean[] secondParent;
                if (selection.equals("tournament")) {
                    secondParent = kTournament(population, K);
                } else {
                    secondParent = randomParent(population);
                }
                Boolean[] child = cross(firstParent, secondParent);
                mutate(child);
                if (function.valueAt(child) > fitnessThreshold(firstParent, secondParent, child)) {
                    newPopulation.add(child);
                }
                childrenCounter++;
                if (newPopulation.size() == MAX_POP) {
                    break;
                }
            }
            while (newPopulation.size() < MIN_POP) {
                Boolean[] firstParent = kTournament(population, K);
                Boolean[] secondParent;
                if (newPopulation.size() == 0) {
                    if (selection.equals("tournament")) {
                        secondParent = kTournament(population, K);
                    } else {
                        secondParent = randomParent(population);
                    }
                } else {
                    if (selection.equals("tournament")) {
                        secondParent = kTournament(newPopulation, K);
                    } else {
                        secondParent = randomParent(newPopulation);
                    }
                }
                Boolean[] child = cross(firstParent, secondParent);
                mutate(child);
                if (function.valueAt(child) > fitnessThreshold(firstParent, secondParent, child)) {
                    newPopulation.add(child);
                }
                childrenCounter++;
                if (getActPress(childrenCounter, oldPopSize) > MAX_SEL_PRESS) {
                    return;
                }
            }
            population.clear();
            population.addAll(newPopulation);
            if (getActPress(childrenCounter, oldPopSize) > MAX_SEL_PRESS) {
                break;
            }
            oldPopSize = childrenCounter;
        }
    }

    private static Boolean[] randomParent(Set<Boolean[]> population) {
        return new ArrayList<>(population).get(Math.abs(random.nextInt()) % population.size());
    }

    private static void mutate(Boolean[] child) {
        for (int i = 0; i < child.length; i++) {
            if (random.nextDouble() < MUTATION_CHANCE) {
                child[i] = !child[i];
            }
        }
    }

    private static double getActPress(double childrenCounter, int oldPopSize) {
        return (childrenCounter + oldPopSize) / oldPopSize;
    }

    private static double fitnessThreshold(Boolean[] firstParent, Boolean[] secondParent, Boolean[] child) {
        Boolean[] bigger = function.valueAt(firstParent) > function.valueAt(secondParent) ? firstParent : secondParent;
        Boolean[] smaller = Arrays.equals(firstParent, bigger) ? secondParent : firstParent;
        return function.valueAt(smaller) +
                compFactor.apply(child) * (function.valueAt(bigger) - function.valueAt(smaller));
    }

    private static Boolean[] cross(Boolean[] firstParent, Boolean[] secondParent) {
        Boolean[] child = new Boolean[firstParent.length];
        for (int i = 0; i < firstParent.length; i++) {
            child[i] = random.nextBoolean() ? firstParent[i] : secondParent[i];
        }
        return child;
    }

    private static Boolean[] kTournament(Set<Boolean[]> population, int k) {
        Set<Boolean[]> candidates = new HashSet<>();
        Boolean[] bestCandidate = null;
        List<Boolean[]> populationList = new ArrayList<>(population);
        Collections.shuffle(populationList);
        while (candidates.size() != k && candidates.size() != population.size()) {
            Boolean[] candidate = populationList.get(Math.abs(random.nextInt()) % population.size());
            if (bestCandidate == null || function.valueAt(candidate) > function.valueAt(bestCandidate)) {
                bestCandidate = candidate;
            }
            candidates.add(candidate);
        }
        return bestCandidate;
    }

    private static void initPopulation() {
        population = new HashSet<>();
        while (population.size() < ((MAX_POP + MIN_POP) / 2)) {
            Boolean[] vector = new Boolean[n];
            for (int j = 0; j < vector.length; j++) {
                vector[j] = random.nextBoolean();
            }
            population.add(vector);
        }
    }

    private static Function<Boolean[], Double> getCompFactor() {
        return booleans -> {
            // some of the compFactor values
            //return getFitness(booleans) / n * 1.0;
            return 0.5;
            //return 0.0;
            //return 0.25;
        };
    }

    private static IFunction getFunction() {
        return vector -> {
            int counter = getFitness(vector);
            if (counter <= 0.8 * vector.length) {
                return (double) counter / vector.length;
            } else if (counter > 0.9 * vector.length) {
                return (2. * counter / vector.length) - 1;
            } else {
                return 0.8;
            }
        };
    }

    private static int getFitness(Boolean[] vector) {
        int counter = 0;
        for (Boolean bit : vector) {
            if (bit) {
                counter++;
            }
        }
        return counter;
    }
}
