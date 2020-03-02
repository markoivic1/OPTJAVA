package hr.fer.zemris.optjava.dz2;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface IFunction {
    int getNumberOfVariables();

    double getValue(double[] point);

    double[] getGradient(double[] point);
}
