package hr.fer.zemris.optjava.dz7;

import java.util.Arrays;
import java.util.Comparator;

/**
 * run ANNTrainer with these arguments: ./07-iris-formatirano.data clonalg 60 0.005 500
 * best solution 149/150
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ClonAlg {
    private int n;
    private int maxiter;
    private int d;
    private double beta;
    private double ro;
    private double mError;

    public ClonAlg(int n, double mError, int maxiter, int d, double beta, double ro) {
        this.n = n;
        this.maxiter = maxiter;
        this.d = d;
        this.beta = beta;
        this.ro = ro;
        this.mError = mError;
    }

    public double[] run(FFANN ffann) {
        double[][] population = new double[n][ffann.getWeightsCount()];
        initPopulation(population);
        for (int iter = 0; iter < maxiter; iter++) {
            double[] affinity = new double[n];
            for (int i = 0; i < population.length; i++) {
                affinity[i] = ffann.getAffinity(population[i]);
            }
            sort(population, affinity);
            double[][] clones = clonePop(population, beta);
            hypermutate(ffann, clones);
            double[] cloneAffinity = new double[clones.length];
            for (int i = 0; i < clones.length; i++) {
                cloneAffinity[i] = ffann.getAffinity(clones[i]);
            }
            sort(clones, cloneAffinity);

            double[][] nextPopulation = new double[n][0];
            selectUnits(clones, nextPopulation);
            double[][] birth = new double[d][ffann.getWeightsCount()];
            initPopulation(birth);
            insertBirthed(birth, nextPopulation, d);
            population = nextPopulation;

            double bestAffinity = ffann.getAffinity(population[0]);
            if (bestAffinity < mError) {
                return population[0];
            }
        }
        return population[0];
    }

    private void insertBirthed(double[][] birth, double[][] nextPopulation, int d) {
        System.arraycopy(birth, 0, nextPopulation, nextPopulation.length - d, d);
    }

    private void selectUnits(double[][] clones, double[][] nextPopulation) {
        System.arraycopy(clones, 0, nextPopulation, 0, nextPopulation.length);
    }

    private void hypermutate(FFANN ffann, double[][] clones) {
        for (double[] clone : clones) {
            double mutationChance = 1 - Math.exp(-ro * ffann.getAffinity(clone));
            Util.mutate(clone, 0, 0.2, mutationChance);
        }
    }

    private double[][] clonePop(double[][] population, double beta) {
        int size = 0;
        for (int i = 1; i < n; i++) {
            size += (int) (beta * n) / i;
        }
        double[][] clones = new double[size][population[0].length];

        int currentPosition = 0;
        for (int i = 1; i < n; i++) {
            int unitClones = (int) (beta * n) / i;
            for (int j = 0; j < unitClones; j++) {
                clones[currentPosition] = population[i - 1].clone();
                currentPosition++;
            }
        }
        return clones;
    }

    static class UnitComparator implements Comparator<Integer> {
        private double[] affinity;

        public UnitComparator(double[] affinity) {
            this.affinity = affinity;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            return Double.compare(affinity[o1], affinity[o2]);
        }
    }

    private void sort(double[][] population, double[] affinity) {
        Integer[] indexes = new Integer[affinity.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new UnitComparator(affinity));
        double[][] tmpPopulation = new double[population.length][0];
        for (int i = 0; i < tmpPopulation.length; i++) {
            tmpPopulation[i] = population[indexes[i]];
        }
        for (int i = 0; i < population.length; i++) {
            population[i] = tmpPopulation[i];
        }
    }

    private void initPopulation(double[][] population) {
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[i].length; j++) {
                population[i][j] = Util.randomInRange(-1, 1);
            }
        }
    }
}
