package hr.fer.zemris.optjava.dz3.neighborhood;

import hr.fer.zemris.optjava.dz3.solutions.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.util.Util;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class BitArrayNaturalNeighborhood implements INeighborhood<BitVectorSolution> {

    private int[] bits;

    public BitArrayNaturalNeighborhood(int[] bits) {
        this.bits = bits;
    }

    @Override
    public BitVectorSolution randomNeighbor(BitVectorSolution solution) {
        byte[] bits = solution.getBits().clone();

        boolean[] bitsAsBool = Util.convertToBool(bits);
        int index = Math.abs(new Random().nextInt()) % (bits.length * 8);
        bitsAsBool[index] = !bitsAsBool[index];

        BitVectorSolution newSolution = solution.newLikeThis();
        newSolution.setBits(Util.convertToBits(bitsAsBool));
        return newSolution;
    }
}
