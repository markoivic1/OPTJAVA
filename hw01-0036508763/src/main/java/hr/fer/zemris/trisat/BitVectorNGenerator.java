package hr.fer.zemris.trisat;

import java.util.Iterator;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector> {

    private BitVector assignment;

    public BitVectorNGenerator(BitVector assignment) {
        this.assignment = assignment;
    }

    // Vraća iterator koji na svaki next() računa sljedećeg susjeda
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<MutableBitVector>() {
            private int index = 0;

            public boolean hasNext() {
                if (index == assignment.getSize()) {
                    return false;
                }
                return true;
            }

            public MutableBitVector next() {
                MutableBitVector mutableBitVector = assignment.copy();
                mutableBitVector.set(index, !assignment.get(index));
                index++;
                return mutableBitVector;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Vraća kompletno susjedstvo kao jedno polje
    public MutableBitVector[] createNeighborhood() {
        MutableBitVector[] vectors = new MutableBitVector[assignment.getSize()];
        int index = 0;
        for (MutableBitVector vector : new BitVectorNGenerator(assignment)) {
            vectors[index] = vector;
            index++;
        }
        return vectors;
    }
}
