package hr.fer.zemris.optjava.dz3.solutions;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DoubleArraySolution extends SingleObjectiveSolution {
    private double[] values;

    public DoubleArraySolution(int size) {
        this.values = new double[size];
    }

    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(values.length);
    }

    public DoubleArraySolution duplicate() {
        DoubleArraySolution solution = new DoubleArraySolution(values.length);
        solution.values = this.values.clone();
        return solution;
    }

    public void randomize(Random random, double[] low, double[] high) {
        for (int i = 0; i < values.length; i++) {
            values[i] = (random.nextDouble() * (high[i] - low[i])) + low[i];
        }
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}
