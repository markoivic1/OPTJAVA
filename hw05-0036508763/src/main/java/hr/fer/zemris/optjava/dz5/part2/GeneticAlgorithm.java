package hr.fer.zemris.optjava.dz5.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * ./data/nug12.dat 64 8 managed to find best solution
 * ./data/els19.dat 64 8 managed to find best solution
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GeneticAlgorithm {
    private static final double COMP_FACTOR = 0.33;
    private static final int K = 2;
    private static final double SUCC_RATIO = 0.30;
    private static final double MUTATION_CHANCE = 0.10;
    private static int[][] distances;
    private static int[][] costs;
    private static int n;
    private static int populationSubdivision;
    private static List<Permutation> population;
    private static List<Integer> totalPopulationSize;
    private static Random random = new Random();
    private static final int maxIter = 30000;
    private static double MAX_SEL_PRESS = 3;
    private static volatile List<Boolean> improvable = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }
        population = new ArrayList<>();
        n = Integer.parseInt(args[1]);
        load(Paths.get(args[0]));
        totalPopulationSize = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            totalPopulationSize.add(0);
        }
        populationSubdivision = Integer.parseInt(args[2]);
        run();
    }

    public static class Calculate implements Runnable {
        private int index;

        public Calculate(int j) {
            this.index = j;
        }

        @Override
        public void run() {
            improvable.set(index - 1, isImprovable(index));
            //System.out.println(getFitness(getBest(index)));
        }
    }

    private static void run() {
        reset(improvable);
        for (int i = 0; i < maxIter; i++) {
            Thread[] threads = new Thread[populationSubdivision];
            for (int j = 1; j < populationSubdivision + 1; j++) {
                if (!improvable.get(j - 1)) {
                    continue;
                }
                threads[j - 1] = new Thread(new Calculate(j));
                threads[j - 1].start();
            }
            for (int j = 1; j < populationSubdivision + 1; j++) {
                if (!improvable.get(j - 1)) {
                    continue;
                }
                while (true) {
                    try {
                        threads[j - 1].join();
                        break;
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
            if (!checkIfImprovable(improvable) && populationSubdivision == 1) {
                printFinalPopulation();
                return;
            }
            if (!checkIfImprovable(improvable)) {
                populationSubdivision--;
                reset(improvable);
                //System.out.println("Decreased subdivisions");
            }
        }
        printFinalPopulation();
        System.out.println("Out of iterations");
    }

    private static void printFinalPopulation() {
        Permutation best = getBest(1);
        System.out.print("Final result has a punishment value of: ");
        System.out.println(getFitness(best));
        System.out.print("Order: ");
        System.out.println(best);
    }

    private static void reset(List<Boolean> improvable) {
        improvable.clear();
        for (int j = 0; j < populationSubdivision; j++) {
            improvable.add(true);
        }
    }

    private static boolean checkIfImprovable(List<Boolean> population) {
        for (Boolean isImprovable : population) {
            if (isImprovable) {
                return true;
            }
        }
        return false;
    }

    private static Permutation getBest(int index) {
        int ratio = population.size() / populationSubdivision;
        int offset;
        if (index == population.size()) {
            offset = population.size() % populationSubdivision;
        } else {
            offset = 0;
        }
        int start = ratio * (index - 1);
        int finish = ratio * index + offset;
        return getParentByTournament(start, finish, finish - start);
    }

    private static boolean isImprovable(int index) {
        int ratio = population.size() / populationSubdivision;
        int offset;
        if (index == population.size()) {
            offset = population.size() % populationSubdivision;
        } else {
            offset = 0;
        }
        int start = ratio * (index - 1);
        int finish = ratio * index + offset;
        List<Permutation> successful = new ArrayList<>();
        List<Permutation> unsuccessful = new ArrayList<>();
        // built in elitism
        successful.add(getParentByTournament(start, finish, finish - start));
        int currentPopSize = 0;
        while (successful.size() < Double.valueOf(n * SUCC_RATIO).intValue()) {
            // get parents
            Permutation firstParent = getParentByTournament(start, finish, K);
            Permutation secondParent = getParentByTournament(start, finish, K);
            while (firstParent.equals(secondParent)) {
                secondParent = getParentByTournament(start, finish, K);
            }
            // cross and mutate children
            List<Permutation> children = cross(firstParent, secondParent);
            mutate(children);
            currentPopSize++;
            // choose randomly one child because two might be too genetically similar.
            if (random.nextBoolean()) {
                if ((getFitness(children.get(0)) < getFitnessThreshold(firstParent, secondParent)) && !successful.contains(children.get(0))) {
                    successful.add(children.get(0));
                } else if (!unsuccessful.contains(children.get(0))) {
                    unsuccessful.add(children.get(0));
                }
            } else {
                if ((getFitness(children.get(1)) < getFitnessThreshold(firstParent, secondParent)) && !successful.contains(children.get(1))) {
                    successful.add(children.get(1));
                } else if (!unsuccessful.contains(children.get(1))) {
                    unsuccessful.add(children.get(1));
                }
            }
            if (calculateActSelPress(currentPopSize, index - 1) > MAX_SEL_PRESS) {
                return false;
            }
        }
        // fill new population with successful and unsuccessful children
        List<Permutation> newPopulation = new ArrayList<>();
        newPopulation.addAll(successful.subList(0, Math.min(n, successful.size())));
        newPopulation.addAll(unsuccessful.subList(0, Math.min(n - newPopulation.size(), unsuccessful.size())));
        // fill population with anything if success ratio has been satisfied
        while (newPopulation.size() != n) {
            Permutation firstParent = getParentByTournament(start, finish, K);
            Permutation secondParent = getParentByTournament(start, finish, K);
            while (firstParent.equals(secondParent)) {
                secondParent = getParentByTournament(start, finish, K);
            }
            List<Permutation> children = cross(firstParent, secondParent);
            currentPopSize++;
            if (newPopulation.contains(children.get(0))) {
                continue;
            }
            newPopulation.add(children.get(0));
        }
        // update population size
        totalPopulationSize.set(index - 1, currentPopSize);
        // replace population
        for (int i = start; i < finish; i++) {
            population.set(i, newPopulation.get(i - start));
        }
        return true;
    }

    private static void mutate(List<Permutation> children) {
        for (Permutation child : children) {
            if (random.nextDouble() > MUTATION_CHANCE) {
                continue;
            }
            int index = random.nextInt(child.size());
            int firstIndex = random.nextInt(child.size() - 1);
            int secondIndex = random.nextInt(child.size() - firstIndex) + firstIndex;
            for (int i = 0; i < secondIndex - firstIndex; i++) {
                int value = child.getFactories().get(firstIndex);
                child.getFactories().remove(firstIndex);
                child.getFactories().add(index, value);
            }
        }
    }

    private static int calculateActSelPress(int currentPopSize, int index) {
        if (totalPopulationSize.get(index) == 0) {
            return 0;
        }
        return (currentPopSize + totalPopulationSize.get(index)) / totalPopulationSize.get(index);
    }

    private static double getFitnessThreshold(Permutation firstParent, Permutation secondParent) {
        int better = getFitness(firstParent);
        int worse = getFitness(secondParent);
        if (better < worse) {
            int tmp = better;
            better = worse;
            worse = tmp;
        }
        return worse + COMP_FACTOR * (better - worse);
    }

    private static int getFitness(Permutation child) {
        int fitness = 0;
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < costs.length; j++) {
                fitness += costs[i][j] * distances[child.getFactories().get(i) - 1][child.getFactories().get(j) - 1];
            }
        }
        return fitness;
    }

    private static List<Permutation> cross(Permutation firstParent, Permutation secondParent) {
        // order crossover
        int firstIndex = random.nextInt(firstParent.size() - 1);
        int secondIndex = random.nextInt(firstParent.size() - firstIndex) + firstIndex;
        Permutation firstChild = new Permutation(new ArrayList<>(firstParent.getFactories().subList(firstIndex, secondIndex)));
        Permutation secondChild = new Permutation(new ArrayList<>(secondParent.getFactories().subList(firstIndex, secondIndex)));
        fillChromosome(secondParent, firstChild, secondIndex);
        fillChromosome(firstParent, secondChild, secondIndex);
        List<Permutation> children = new ArrayList<>();
        children.add(firstChild);
        children.add(secondChild);
        return children;
    }

    private static void fillChromosome(Permutation secondParent, Permutation child, int index) {
        // order crossover
        int insertIndex = 0;
        int suffixCounter = 0;
        boolean insertInFront = false;
        int numberOfSuffixes = secondParent.size() - index;
        while (child.size() != secondParent.size()) {
            index = index % secondParent.size();
            if (suffixCounter == numberOfSuffixes) {
                insertInFront = true;
            }
            Integer element = secondParent.getFactories().get(index);
            if (child.getFactories().contains(element)) {
                index++;
                continue;
            }
            if (insertInFront) {
                child.getFactories().add(insertIndex, element);
                insertIndex++;
                index++;
            } else {
                child.getFactories().add(element);
                suffixCounter++;
                index++;
            }
        }
    }

    private static Permutation getParentByTournament(int start, int finish, int k) {
        List<Permutation> subPopulation = population.subList(start, finish);
        List<Permutation> candidates = new ArrayList<>();
        if (k == subPopulation.size()) {
            candidates.addAll(subPopulation);
        } else {
            while (candidates.size() != k) {
                Permutation candidate = subPopulation.get(random.nextInt(subPopulation.size()));
                if (candidates.contains(candidate)) {
                    continue;
                }
                candidates.add(candidate);
            }
        }
        Permutation best = candidates.get(0);
        for (int i = 1; i < candidates.size(); i++) {
            if (getFitness(candidates.get(i)) < getFitness(best)) {
                best = candidates.get(i);
            }
        }
        return best;
    }

    private static void load(Path path) throws IOException {
        String[] text = Files.readString(path).split("\n\n");
        int n = Integer.parseInt(text[0]);
        initPopulation(n);
        distances = new int[n][n];
        costs = new int[n][n];
        fill(distances, n, text[1].trim().split(" +|\n"));
        fill(costs, n, text[2].trim().split(" +|\n"));
    }

    private static void initPopulation(int unitSize) {
        for (int i = 0; i < n; i++) {
            population.add(new Permutation());
            for (int j = 0; j < unitSize; j++) {
                int randomNumber = random.nextInt(unitSize) + 1;
                if (population.get(i).getFactories().contains(randomNumber)) {
                    j--;
                    continue;
                }
                population.get(i).add(randomNumber);
            }
        }
    }

    private static void fill(int[][] data, int n, String[] stringValues) {
        int offset = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (stringValues[i * n + j + offset].equals("")) {
                    offset++;
                }
                data[i][j] = Integer.parseInt(stringValues[i * n + j + offset]);
            }
        }
    }
}
