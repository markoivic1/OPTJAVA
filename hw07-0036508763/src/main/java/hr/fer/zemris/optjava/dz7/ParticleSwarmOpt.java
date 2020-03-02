package hr.fer.zemris.optjava.dz7;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * run ANNTrainer with these arguments: ./07-iris-formatirano.data pso-a 60 0.005 1000
 * ./07-iris-formatirano.data pso-b-5 20 0.005 500
 * best solution 149/150
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ParticleSwarmOpt {

    private int n;
    private double mError;
    private int maxiter;
    private double weightMax = 0.9;
    private double weightMin = 0.4;
    private double weight;
    private double c1 = 2;
    private double c2 = 2;

    public ParticleSwarmOpt(int n, double mError, int maxiter, double weightMax, double weightMin, double weight, double c1, double c2) {
        this.n = n;
        this.mError = mError;
        this.maxiter = maxiter;
        this.weightMax = weightMax;
        this.weightMin = weightMin;
        this.weight = weight;
        this.c1 = c1;
        this.c2 = c2;
    }

    public double[] run(int neighbours, FFANN ffan) {
        double[][] particles = new double[n][ffan.getWeightsCount()];
        double[][] velocities = new double[n][ffan.getWeightsCount()];

        double[][] personalBest = new double[n][ffan.getWeightsCount()];
        double[] personalBestAffinity = new double[n];

        double[] localBestAffinity = neighbours > 0 ? new double[n] : new double[1];
        double[][] localBest = new double[n][ffan.getWeightsCount()];

        // init
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < ffan.getWeightsCount(); j++) {
                particles[i][j] = Util.randomInRange(-1, 1);
                velocities[i][j] = Util.randomInRange(-1, 1);
            }
            personalBestAffinity[i] = ffan.getAffinity(particles[i]);
            personalBest[i] = particles[i];
        }

        // main loop
        for (int i = 0; i < maxiter; i++) {

            // calculates current affinities and them accordingly
            double[] currentAffinity = new double[n];
            for (int j = 0; j < n; j++) {
                double affinity = ffan.getAffinity(particles[j]);
                currentAffinity[j] = affinity;
                if (affinity < personalBestAffinity[j]) {
                    personalBest[j] = particles[j];
                    personalBestAffinity[j] = affinity;
                }
            }

            // calculates local best particles and their affinities
            calculateLocalBest(neighbours, personalBest, personalBestAffinity, localBest, localBestAffinity, n);

            updateWeight(i);
            // calculates velocity and moves particle
            for (int j = 0; j < n; j++) {
                RealMatrix inertia = MatrixUtils.createColumnRealMatrix(velocities[j]).scalarMultiply(weight);
                RealMatrix personalVelocity = MatrixUtils.createRealMatrix(getRandomMatrix(ffan.getWeightsCount()))
                        .scalarMultiply(c1).multiply(
                                MatrixUtils.createColumnRealMatrix(personalBest[j]).subtract(MatrixUtils.createColumnRealMatrix(particles[j])));
                RealMatrix localVelocity = MatrixUtils.createRealMatrix(getRandomMatrix(ffan.getWeightsCount()))
                        .scalarMultiply(c2).multiply(
                                MatrixUtils.createColumnRealMatrix(localBest[j]).subtract(MatrixUtils.createColumnRealMatrix(particles[j])));
                personalVelocity = MatrixUtils.createColumnRealMatrix(limitVelocity(personalVelocity.getColumn(0)));
                localVelocity = MatrixUtils.createColumnRealMatrix(limitVelocity(localVelocity.getColumn(0)));
                RealMatrix currentVelocity = inertia.add(personalVelocity).add(localVelocity);
                particles[j] = MatrixUtils.createColumnRealMatrix(particles[j]).add(currentVelocity).getColumn(0);
                velocities[j] = currentVelocity.getColumn(0);
            }
            int best = getBest(localBestAffinity);
            if (localBestAffinity[best] < mError) {
                return localBest[best];
            }
        }

        int best = getBest(localBestAffinity);
        return localBest[best];
    }

    private double[] limitVelocity(double[] velocity) {
        for (int i = 0; i < velocity.length; i++) {
            if (velocity[i] > 1) {
                velocity[i] = 1;
            } else if (velocity[i] < -1) {
                velocity[i] = -1;
            }
        }
        return velocity;
    }

    private void calculateLocalBest(int neighbours, double[][] personalBest, double[] personalBestAffinity, double[][] localBest, double[] localBestAffinity, int size) {
        for (int i = 0; i < n; i++) {
            int index = i - neighbours;
            double[] currentBest = personalBest[i];
            double currentBestAffinity = personalBestAffinity[i];
            index++;
            while (index < neighbours + i) {

                double currentParticleAffinity = index >= 0 ? personalBestAffinity[index % size] : personalBestAffinity[n + index];

                if (currentBestAffinity > currentParticleAffinity) {
                    currentBestAffinity = currentParticleAffinity;
                    currentBest = index >= 0 ? personalBest[index % size] : personalBest[n + index];
                }
                index++;
            }
            localBest[i] = currentBest;
            localBestAffinity[i] = currentBestAffinity;
        }
    }

    private int getBest(double[] personalBestAffinity) {
        int best = 0;
        for (int j = 1; j < personalBestAffinity.length; j++) {
            if (personalBestAffinity[j] < personalBestAffinity[best]) {
                best = j;
            }
        }
        return best;
    }


    private double[][] getRandomMatrix(int size) {
        double[][] matrix = new double[size][size];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][i] = Util.randomInRange(0, 1);
        }
        return matrix;
    }

    private void updateWeight(int t) {
        weight = (((double) maxiter - t) / maxiter) * (weightMax - weightMin) + weightMin;
    }
}
