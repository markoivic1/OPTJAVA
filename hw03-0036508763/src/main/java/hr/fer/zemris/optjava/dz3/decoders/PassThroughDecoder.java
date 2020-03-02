package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

    public double[] decode(DoubleArraySolution solution) {
        return solution.getValues();
    }

    public void decode(DoubleArraySolution solution, double[] array) {
        array = solution.getValues();
    }

}
