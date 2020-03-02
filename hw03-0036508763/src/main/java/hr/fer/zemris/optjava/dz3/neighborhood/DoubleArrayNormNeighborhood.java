package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DoubleArrayNormNeighborhood implements INeighborhood<DoubleArraySolution>{
    private double[] deltas;
    protected Random random;

    public DoubleArrayNormNeighborhood(double[] deltas) {
        this.deltas = deltas;
    }

    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        double[] values = solution.getValues().clone();
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i] + (random.nextGaussian() * (deltas[1] - deltas[0]) + deltas[0]);
        }
        DoubleArraySolution newSolution = solution.newLikeThis();
        newSolution.setValues(values);
        return newSolution;
    }
}
