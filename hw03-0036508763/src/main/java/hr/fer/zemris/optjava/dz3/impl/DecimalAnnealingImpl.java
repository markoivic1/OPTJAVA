package hr.fer.zemris.optjava.dz3.impl;

import hr.fer.zemris.optjava.dz3.GeometricTempSchedule;
import hr.fer.zemris.optjava.dz3.IFunction;
import hr.fer.zemris.optjava.dz3.IOptAlgorithm;
import hr.fer.zemris.optjava.dz3.SimulatedAnnealing;
import hr.fer.zemris.optjava.dz3.decoders.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.neighborhood.BitArrayNaturalNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solutions.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DecimalAnnealingImpl {
    public static void main(String[] args) {
        IFunction function = new IFunction() {
            public double valueAt(double[] array) {
                //return Math.abs(array[0]);
                return Math.abs(array[0]) + 1 - Math.cos(5 * array[0]);
                //return array[0] * array[0] + 1;
                //return -Math.abs(array[0]) + 1;
            }
        };
        IFunction fitnessCalculator = new IFunction() {
            @Override
            public double valueAt(double[] array) {
                return function.valueAt(array);
            }
        };
        IOptAlgorithm<DoubleArraySolution> algorithm = initAlgorithm(function, fitnessCalculator);

        algorithm.run();
    }

    private static IOptAlgorithm<DoubleArraySolution> initAlgorithm(IFunction function, IFunction fitnessCalculator) {
        DoubleArraySolution doubleArraySolution = new DoubleArraySolution(1);
        doubleArraySolution.randomize(new Random(), new double[]{-5}, new double[]{5});
        INeighborhood<DoubleArraySolution> doubleNeighborhood = new DoubleArrayUnifNeighborhood(new double[]{-5, 5});
        return new SimulatedAnnealing<>(new PassThroughDecoder(),
                doubleNeighborhood, doubleArraySolution, function, true,
                new GeometricTempSchedule(0.95, 1000, 1000, 10000));
    }
}
