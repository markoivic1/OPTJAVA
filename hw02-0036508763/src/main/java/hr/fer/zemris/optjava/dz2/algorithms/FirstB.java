package hr.fer.zemris.optjava.dz2.algorithms;

import hr.fer.zemris.optjava.dz2.DrawTrajectory;
import hr.fer.zemris.optjava.dz2.IFunction;
import hr.fer.zemris.optjava.dz2.IHFunction;
import hr.fer.zemris.optjava.dz2.NumOptAlgorithms;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class FirstB {

    private static IHFunction function = new IHFunction() {

        public RealMatrix getHesseMatrix(double[] point) {
            double[][] data = new double[2][2];

            data[0][0] = 2;
            data[1][1] = 2;

            return MatrixUtils.createRealMatrix(data);
        }

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

    public static void execute(int iterations, double[] point) {
        DrawTrajectory.name = "firstB";
        NumOptAlgorithms.newtonsMethod(function, iterations, point);
    }
}
