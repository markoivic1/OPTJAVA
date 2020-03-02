package hr.fer.zemris.optjava.dz2.algorithms;

import hr.fer.zemris.optjava.dz2.*;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class FirstA {

    private static IFunction function = new IFunction() {

        public int getNumberOfVariables() {
            return 2;
        }

        public double getValue(double[] point) {
            return Math.pow(point[0], 2) + Math.pow(point[1] + 1, 2);
        }

        public double[] getGradient(double[] point) {
            double[] gradient = new double[getNumberOfVariables()];
            gradient[0] = 2 * point[0];
            gradient[1] = 2 * point[1] - 2;
            return gradient;
        }
    };

    public static void execute(int numberOfIterations, double[] point) {
        DrawTrajectory.name = "firstA";
        NumOptAlgorithms.gradientDescent(function, numberOfIterations, point);
    }

}
