package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DoubleArrayRandomUnifNeighborhood implements INeighborhood<DoubleArraySolution> {
    private double[] deltas;
    Random random;
    private static final double CHANCE = 0.2;

    public DoubleArrayRandomUnifNeighborhood(double[] deltas) {
        this.deltas = deltas;
        random = new Random();
    }
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        double[] values = solution.getValues().clone();
        for (int i = 0; i < values.length; i++) {
            if (new Random().nextDouble() < CHANCE) {
                values[i] = values[i] + (random.nextDouble() * (deltas[1] - deltas[0]) + deltas[0]);
            }
        }
        DoubleArraySolution newSolution = solution.newLikeThis();
        newSolution.setValues(values);
        return newSolution;
    }
}
