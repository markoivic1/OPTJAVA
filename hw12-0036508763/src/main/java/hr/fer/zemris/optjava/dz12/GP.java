package hr.fer.zemris.optjava.dz12;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static hr.fer.zemris.optjava.dz12.GPUtil.*;

/**
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GP {

    private static int[][] map;
    private static int maxIter;
    private static int popSize;
    private static int minFitness;
    private static String savePath;
    private static final int INIT_DEPTH = 6;
    private static final int MAX_DEPTH = 20;
    private static final int MAX_NODES = 200;
    private static final int ACTIONS_LEFT = 600;
    private static final int K = 7;
    private static final Random random = new Random();
    private static final double REPRODUCTION_CHANCE = 0.01; // 0.01
    private static final double MUTATION_CHANCE = 0.14;     // 0.14
    private static final double CROSSOVER_CHANCE = 0.85;    // 0.85
    private static final double PENALTY = 0.9;
    private static List<IterationListener> listeners = new ArrayList<>();

    public static Node calculateAnt(int[][] map, int maxIter, int popSize, int minFitness, String savePath, Ant ant) {
        GP.map = map;
        GP.maxIter = maxIter;
        GP.popSize = popSize;
        GP.minFitness = minFitness;
        GP.savePath = savePath;
        Node best = run(ant);
        Util.saveProgram(best, savePath);
        return best;
    }

    public static void registerListener(IterationListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(IterationListener listener) {
        listeners.remove(listener);
    }

    private static Node run(Ant ant) {
        List<Node> population = initPopulation(INIT_DEPTH, MAX_NODES, popSize);
        int[] foodEaten = new int[population.size()];
        double[] fitness = new double[population.size()];
        evaluate(population, ant, map, foodEaten, fitness);
        for (int iter = 0; iter < maxIter; iter++) {
            notifyListeners(iter + 1);
            List<Node> newPopulation = new ArrayList<>();
            int[] newFoodEaten = new int[population.size()];
            double[] newFitness = new double[population.size()];
            int best = getBest(population, fitness);
            newPopulation.add(population.get(best));
            newFoodEaten[0] = foodEaten[best];
            newFitness[0] = fitness[best];
            while (newPopulation.size() != popSize) {
                double chance = random.nextDouble();
                if (chance < REPRODUCTION_CHANCE) {
                    reproduction(population, ant, foodEaten, fitness, newPopulation, newFoodEaten, newFitness);
                } else if (chance > 1 - MUTATION_CHANCE) {
                    mutation(population, ant, foodEaten, fitness, newPopulation, newFoodEaten, newFitness);
                } else {
                    crossover(population, ant, foodEaten, fitness, newPopulation, newFoodEaten, newFitness);
                }
            }
            population = newPopulation;
            fitness = newFitness;
            foodEaten = newFoodEaten;
            int bestIndex = getBest(population, fitness);
            if (fitness[bestIndex] >= minFitness) {
                notifyListeners(maxIter);
                System.out.println("Fitness: " + fitness[bestIndex]);
                return population.get(bestIndex);
            }
        }
        int bestIndex = getBest(population, fitness);
        System.out.println("Fitness: " + fitness[bestIndex]);
        return population.get(bestIndex);
    }

    private static void notifyListeners(int iter) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).notifyListener(iter);
        }
    }

    private static void mutation(List<Node> population, Ant ant, int[] foodEaten, double[] fitness, List<Node> newPopulation, int[] newFoodEaten, double[] newFitness) {
        int parentIndex = kTournament(fitness, K);
        Node node = population.get(parentIndex);
        Node mutatedNode = mutate(node, MAX_DEPTH, MAX_NODES);
        while (mutatedNode == null) {
            mutatedNode = mutate(node, MAX_DEPTH, MAX_NODES);
        }
        evaluate(mutatedNode, ant, map, newFoodEaten, newFitness, newPopulation.size());
        if (foodEaten[parentIndex] == newFoodEaten[newPopulation.size()]) {
            newFitness[newPopulation.size()] *= PENALTY;
        }
        newPopulation.add(mutatedNode);
    }

    private static void crossover(List<Node> population, Ant ant, int[] foodEaten, double[] fitness, List<Node> newPopulation, int[] newFoodEaten, double[] newFitness) {
        while (true) {
            int firstParentIndex = kTournament(fitness, K);
            Node firstParent = population.get(firstParentIndex);
            Node secondParent = population.get(kTournament(fitness, K));
            while (firstParent == secondParent) {
                secondParent = population.get(kTournament(fitness, K));
            }
            Node child = GPUtil.crossover(firstParent, secondParent, MAX_DEPTH, MAX_NODES);
            if (child != null) {
                evaluate(child, ant, map, newFoodEaten, newFitness, newPopulation.size());
                if (foodEaten[firstParentIndex] == newFoodEaten[newPopulation.size()]) {
                    newFitness[newPopulation.size()] *= PENALTY;
                }
                newPopulation.add(child);
                break;
            }
        }
    }

    private static void reproduction(List<Node> population, Ant ant, int[] foodEaten, double[] fitness, List<Node> newPopulation, int[] newFoodEaten, double[] newFitness) {
        int parentIndex = kTournament(fitness, K);
        Node child = population.get(parentIndex).copy();
        evaluate(child, ant, map, newFoodEaten, newFitness, newPopulation.size());
        if (foodEaten[parentIndex] == newFoodEaten[newPopulation.size()]) {
            newFitness[newPopulation.size()] *= PENALTY;
        }
        newPopulation.add(child);
    }
}
