package hr.fer.zemris.optjava.dz9;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface MOOPProblem {
    int getNumberOfObjectives();

    void evaluateSolution(double[] solution, double[] objectives);

    double[] evaluateSolution(double[] solution);

    void limit(double[] solution);

    double[] generate(Random random);
}
