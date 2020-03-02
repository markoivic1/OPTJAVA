package hr.fer.zemris.optjava.dz2.algorithms;

import hr.fer.zemris.optjava.dz2.DrawTrajectory;
import hr.fer.zemris.optjava.dz2.IFunction;
import hr.fer.zemris.optjava.dz2.NumOptAlgorithms;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SecondA {

    private static IFunction function = new IFunction() {
        @Override
        public int getNumberOfVariables() {
            return 2;
        }

        @Override
        public double getValue(double[] point) {
            return Math.pow(point[0] - 1, 2) + 10 * Math.pow(point[1] - 2, 2);
        }

        @Override
        public double[] getGradient(double[] point) {
            double[] gradient = new double[2];
            gradient[0] = 2 * point[0] - 2;
            gradient[1] = 10 * (2 * point[1] - 4);
            return gradient;
        }
    };

    public static void execute(int iterations, double[] point) {
        DrawTrajectory.name = "secondA";
        NumOptAlgorithms.gradientDescent(function, iterations, point);
    }
}
