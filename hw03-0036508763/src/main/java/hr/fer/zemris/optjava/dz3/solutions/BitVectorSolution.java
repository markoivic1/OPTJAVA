package hr.fer.zemris.optjava.dz3.solutions;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class BitVectorSolution extends SingleObjectiveSolution {
    private byte[] bits;

    public BitVectorSolution(int size) {
        this.bits = new byte[size / 8 + 1];
    }

    public BitVectorSolution newLikeThis() {
        return new BitVectorSolution(this.bits.length);
    }

    public BitVectorSolution duplicate() {
        BitVectorSolution solution = new BitVectorSolution(bits.length);
        solution.bits = this.bits.clone();
        return solution;
    }

    public void randomize(Random random) {
        StringBuilder bit = new StringBuilder();
        for (int i = 0; i < bits.length * 8; i++) {
            for (int j = 0; j < 8 * bits.length; j++) {
                bit.append(random.nextBoolean() == true ? "1" : "0");
            }
        }
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (byte) Integer.parseInt(bit.substring(i, i + 8), 2);
        }
    }

    public byte[] getBits() {
        return bits;
    }

    public void setBits(byte[] bits) {
        this.bits = bits;
    }
}
