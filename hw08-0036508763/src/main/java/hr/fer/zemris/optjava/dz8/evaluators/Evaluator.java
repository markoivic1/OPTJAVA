package hr.fer.zemris.optjava.dz8.evaluators;

import hr.fer.zemris.optjava.dz8.IReadOnlyDataset;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface Evaluator {
    double getAffinity(double[] parameters, IReadOnlyDataset dataset);

    int getSize();

    double[] generateUnit(int dimension, double lower, double upper, Random random);
}
