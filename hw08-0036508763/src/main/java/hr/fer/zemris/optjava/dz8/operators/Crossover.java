package hr.fer.zemris.optjava.dz8.operators;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface Crossover {
    double[] cross(double[] target, double[] mutant, double chance, Random random);
}
