package hr.fer.zemris.optjava.dz3;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GeometricTempSchedule implements ITempSchedule {
    private double alpha;
    private double tInitial;
    private double tCurrent;
    private int innerLimit;
    private int outerLimit;
    private int innerCounter;
    private int outerCounter;

    public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
        this.alpha = alpha;
        this.tInitial = tInitial;
        this.tCurrent = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
        this.innerCounter = 0;
        this.outerCounter = 0;
    }

    public double getNextTemperature() {
        tCurrent *= alpha;
        return tCurrent;
    }

    public int getInnerLoopCounter() {
        innerCounter++;
        if (innerCounter == this.innerLimit) {
            innerCounter = -1;
        }
        return innerCounter;
    }

    public int getOuterLoopCounter() {
        innerCounter = 0;
        outerCounter++;
        if (outerCounter == this.outerLimit) {
            return -1;
        }
        return outerCounter;
    }
}
