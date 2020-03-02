package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.BitVectorSolution;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class NaturalBinaryDecoder extends BitVectorDecoder {

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int bitsPerVariable) {
        super(mins, maxs, bits, bitsPerVariable);
    }

    public NaturalBinaryDecoder(double bottom, double top, int bitsPerVariable, int n) {
        super(bottom, top, bitsPerVariable, n);
    }

    public double[] decode(BitVectorSolution solution) {
        double[] values = new double[this.getDimension()];
        byte[] bits = solution.getBits();
        String stringBits = "";
        for (int i = 0; i < bits.length; i++) {
            stringBits += String.format("%8s", Integer.toBinaryString(bits[i] & 0xFF)).replace(' ', '0');;
        }
        for (int i = 0, j = 0; j < this.getDimension(); i += this.bits[j], j++) {
            int value = Integer.parseInt(stringBits.substring(i, i + this.bits[j]), 2);
            values[j] = this.mins[j] + value / (Math.pow(2, this.bits[j]) - 1.) * (this.maxs[j] - this.mins[j]);
        }
        return values;
    }

    public void decode(BitVectorSolution solution, double[] array) {
        double[] decodedArray = this.decode(solution);
        for (int i = 0; i < decodedArray.length; i++) {
            array[i] = decodedArray[i];
        }
    }
}
