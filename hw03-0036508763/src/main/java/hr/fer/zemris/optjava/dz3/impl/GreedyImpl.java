package hr.fer.zemris.optjava.dz3.impl;

import hr.fer.zemris.optjava.dz3.GreedyAlgorithm;
import hr.fer.zemris.optjava.dz3.IFunction;
import hr.fer.zemris.optjava.dz3.IOptAlgorithm;
import hr.fer.zemris.optjava.dz3.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GreedyImpl {
    public static void main(final String[] args) {
        IFunction function = new IFunction() {
            public double valueAt(double[] array) {
                //return Math.abs(array[0]);
                return Math.abs(array[0]) + 1 - Math.cos(5 * array[0]);
                //return -Math.abs(array[0]) + 1;
            }
        };
        DoubleArraySolution doubleArraySolution = new DoubleArraySolution(1);
        doubleArraySolution.randomize(new Random(), new double[]{-5}, new double[]{5});
        IOptAlgorithm<DoubleArraySolution> algorithm = new GreedyAlgorithm<DoubleArraySolution>(
                new PassThroughDecoder(), new DoubleArrayUnifNeighborhood(new double[]{-0.1, 0.1}),
                doubleArraySolution, function, true);
        algorithm.run();
    }
}
