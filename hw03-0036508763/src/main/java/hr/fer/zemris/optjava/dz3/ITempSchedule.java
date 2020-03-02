package hr.fer.zemris.optjava.dz3;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface ITempSchedule {
    double getNextTemperature();

    int getInnerLoopCounter();

    int getOuterLoopCounter();

}
