package hr.fer.zemris.optjava.dz3.decoders;

import hr.fer.zemris.optjava.dz3.solutions.BitVectorSolution;

import java.util.Arrays;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {
    double[] mins;
    double[] maxs;
    int[] bits;
    int n;
    int totalBits;

    public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int bitsPerVariable) {
        this.mins = mins;
        this.maxs = maxs;
        this.bits = bits;
        this.n = bits.length / bitsPerVariable;
        this.totalBits = bitsPerVariable * bits.length;
    }

    public BitVectorDecoder(double bottom, double top, int bitsPerVariable, int n) {
        this.n = n;
        this.totalBits = bitsPerVariable * n;
        double quant = 1. / (Math.pow(2, bitsPerVariable) - 1) * (top - bottom);
        int numberOfBits = (int) Math.ceil(Math.log10((top - bottom) / quant + 1) / Math.log10(2));
        this.bits = new int[n];
        this.mins = new double[n];
        this.maxs = new double[n];
        Arrays.fill(bits, numberOfBits);
        Arrays.fill(mins, bottom);
        Arrays.fill(maxs, top);
    }

    public int getTotalBits() {
        return totalBits;
    }

    public int getDimension() {
        return bits.length;
    }

    public abstract double[] decode(BitVectorSolution solution);

    public abstract void decode(BitVectorSolution solution, double[] array);

}
