package hr.fer.zemris.optjava.dz2.algorithms;

import hr.fer.zemris.optjava.dz2.DrawTrajectory;
import hr.fer.zemris.optjava.dz2.IHFunction;
import hr.fer.zemris.optjava.dz2.NumOptAlgorithms;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SecondB {

    private static IHFunction function = new IHFunction() {
        @Override
        public RealMatrix getHesseMatrix(double[] point) {
            double[][] matrix = new double[2][2];
            matrix[0][0] = 2;
            matrix[1][1] = 20;
            return MatrixUtils.createRealMatrix(matrix);
        }

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
        DrawTrajectory.name = "secondB";
        NumOptAlgorithms.newtonsMethod(function, iterations, point);
    }
}
