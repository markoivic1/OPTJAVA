package hr.fer.zemris.trisat;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class BitVector {

    private boolean[] bits;

    public BitVector(Random rand, int numberOfBits) {
        bits = new boolean[numberOfBits];
        for (int i = 0; i < numberOfBits; i++) {
            bits[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean... bits) {
        this.bits = bits;
    }

    public BitVector(int n) {
        this.bits = new boolean[n];
    }

    // vraća vrijednost index-te varijable
    public boolean get(int index) {
        return bits[index];
    }

    // vraća broj varijabli koje predstavlja
    public int getSize() {
        return bits.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getSize(); i++) {
            sb.append(bits[i] ? "1" : "0");
        }
        return sb.toString();
    }

    // vraća promjenjivu kopiju trenutnog rješenja
    public MutableBitVector copy() {
        return new MutableBitVector(bits.clone());
    }

    boolean[] getBits() {
        return bits;
    }

    void setBits(boolean[] bits) {
        this.bits = bits;
    }
}
