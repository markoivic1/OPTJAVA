package hr.fer.zemris.trisat;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class MutableBitVector extends BitVector {

    public MutableBitVector(boolean... bits) {
        super(bits);
    }

    public MutableBitVector(int n) {
        super(n);
    }

    // zapisuje predanu vrijednost u zadanu varijablu
    public void set(int index, boolean value) {
        boolean[] bits = super.getBits();
        bits[index] = value;
        super.setBits(bits);
    }
}
