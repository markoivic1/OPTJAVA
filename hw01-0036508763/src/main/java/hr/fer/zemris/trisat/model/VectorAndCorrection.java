package hr.fer.zemris.trisat.model;

import hr.fer.zemris.trisat.BitVector;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class VectorAndCorrection {
    private BitVector vector;
    private double correction;

    public VectorAndCorrection(BitVector vector, double correction) {
        this.vector = vector;
        this.correction = correction;
    }

    public BitVector getVector() {
        return vector;
    }

    public void setVector(BitVector vector) {
        this.vector = vector;
    }

    public double getCorrection() {
        return correction;
    }

    public void setCorrection(double correction) {
        this.correction = correction;
    }
}
