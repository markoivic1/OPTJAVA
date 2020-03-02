package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface IHFunction extends IFunction {
    RealMatrix getHesseMatrix(double[] point);
}
