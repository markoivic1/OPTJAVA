package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.BitVectorSolution;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GreyBinaryDecoder extends BitVectorDecoder {

    public GreyBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public double[] decode(BitVectorSolution solution) {
        throw new UnsupportedOperationException();
    }

    public void decode(BitVectorSolution solution, double[] array) {
        throw new UnsupportedOperationException();
    }
}
