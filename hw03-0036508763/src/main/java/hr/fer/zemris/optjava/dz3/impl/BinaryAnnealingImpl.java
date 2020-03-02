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
public class BinaryAnnealingImpl {
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

        IOptAlgorithm<BitVectorSolution> bitVectorAlgorithm = initAlgorithm(function, fitnessCalculator);

        bitVectorAlgorithm.run();
    }

    private static IOptAlgorithm<BitVectorSolution> initAlgorithm(IFunction function, IFunction fitnessCalculator) {
        BitVectorSolution bitVectorSolution = new BitVectorSolution(10 * 1);
        bitVectorSolution.randomize(new Random());
        int[] bits = new int[] {10};
        INeighborhood<BitVectorSolution> bitArrayNeighborhood = new BitArrayNaturalNeighborhood(bits);
        return new SimulatedAnnealing<>(
                new NaturalBinaryDecoder(-2, 2, 10, 1),
                bitArrayNeighborhood,
                bitVectorSolution,
                function,
                true,
                new GeometricTempSchedule(0.95, 100, 1000, 1000));
    }
}
