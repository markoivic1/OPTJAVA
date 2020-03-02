package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.evaluators.Evaluator;
import hr.fer.zemris.optjava.dz8.operators.Crossover;

import java.util.*;

/**
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DEAlgorithm implements Algorithm {

    private IReadOnlyDataset dataset;
    private Crossover crossover;
    private int popSize;
    private double merr;
    private int maxIter;
    private Random random;
    private static final double F = 0.5;
    private static final double CHANCE = 0.5;
    private static final double LOWER = -1;
    private static final double UPPER = 1;

    public DEAlgorithm(IReadOnlyDataset dataset, Crossover crossover, int popSize, double merr, int maxIter) {
        this.dataset = dataset;
        this.crossover = crossover;
        this.popSize = popSize;
        this.merr = merr;
        this.maxIter = maxIter;
        this.random = new Random();
    }

    @Override
    public double[] run(Evaluator evaluator) {
        double[][] population = new double[popSize][evaluator.getSize()];
        initPopulation(population, evaluator);
        double[] evaluated = new double[popSize];
        evaluate(population, evaluated, evaluator);
        for (int iter = 0; iter < maxIter; iter++) {
            double[][] trials = new double[popSize][evaluator.getSize()];
            for (int i = 0; i < popSize; i++) {
                List<Integer> indexes = new ArrayList<>();
                indexes.add(getBestIndex(evaluated));
                while (indexes.size() != 3) {
                    int index = random.nextInt(popSize);
                    if (index == i) {
                        continue;
                    }
                    if (!indexes.contains(index)) {
                        indexes.add(index);
                    }
                }
                double[] mutantVector = new double[population[i].length];
                for (int j = 0; j < mutantVector.length; j++) {
                    mutantVector[j] = population[indexes.get(0)][j] +
                            F * (population[indexes.get(1)][j] - population[indexes.get(2)][j]);
                }
                trials[i] = crossover.cross(population[i], mutantVector, CHANCE, random);
            }
            for (int i = 0; i < popSize; i++) {
                double trialAffinity = evaluator.getAffinity(trials[i], dataset);
                if (trialAffinity <= evaluated[i]) {
                    population[i] = trials[i];
                    evaluated[i] = trialAffinity;
                }
            }
            for (int i = 0; i < popSize; i++) {
                if (evaluated[i] < merr) {
                    return population[getBestIndex(evaluated)];
                }
            }
        }
        return population[getBestIndex(evaluated)];
    }

    private int getBestIndex(double[] evaluated) {
        Integer[] indexes = new Integer[popSize];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new IndexComparator(evaluated));
        return indexes[0];
    }

    private static class IndexComparator implements Comparator<Integer>  {
        private double[] affinities;

        public IndexComparator(double[] affinities) {
            this.affinities = affinities;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            return Double.compare(affinities[o1], affinities[o2]);
        }
    }

    private void evaluate(double[][] population, double[] evaluated, Evaluator evaluator) {
        for (int i = 0; i < evaluated.length; i++) {
            evaluated[i] = evaluator.getAffinity(population[i], dataset);
        }
    }

    private void initPopulation(double[][] population, Evaluator evaluator) {
        for (int i = 0; i < population.length; i++) {
                population[i] = evaluator.generateUnit(population[i].length, LOWER, UPPER, random);
        }
    }
}
