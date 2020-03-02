package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class NumOptAlgorithms {

    private static final double ZERO = 0.0001;

    public static double[] gradientDescent(IFunction function, int maxIterations, double[] startingPoint) {
        double[] point = new double[function.getNumberOfVariables()];
        if (startingPoint == null) {
            for (int i = 0; i < function.getNumberOfVariables(); i++) {
                // hardcoded values
                point[i] = Util.generateNumberBetween(-5, 5);
            }
        } else {
            point = startingPoint;
        }

        DrawTrajectory dt = new DrawTrajectory();
        dt.addPoint(point);
        double[] gradient;
        for (int i = 0; i < maxIterations; i++) {
            gradient = function.getGradient(point);
            if (validSolution(gradient)) {
                System.out.print("Final solution: ");
                printPoint(point);
                System.out.println("Number of needed iterations: " + i);

                dt.addPoint(point);
                System.out.println("Now drawing points...");
                dt.drawPoints();
                return point;
            }

            printPoint(point);
            RealMatrix gradientChange = MatrixUtils.createColumnRealMatrix(gradient)
                    .scalarMultiply(Util.calculateLambda
                            (function, point, MatrixUtils.createColumnRealMatrix(gradient).scalarMultiply(-1)));
            RealMatrix newPoint = MatrixUtils.createColumnRealMatrix(point).subtract(gradientChange);
            point = newPoint.getColumn(0);
            dt.addPoint(point);
            if (i == maxIterations - 1) {
                return point;
            }
        }
        System.out.println("Unable to find solution");
        return null;
    }

    public static double[] newtonsMethod(IHFunction ihFunction, int numberOfIterations, double[] startingPoint) {
        double[] point;
        if (startingPoint == null) {
            point = new double[ihFunction.getNumberOfVariables()];
            for (int i = 0; i < ihFunction.getNumberOfVariables(); i++) {
                point[i] = Util.generateNumberBetween(-5, 5);
            }
        } else {
            point = startingPoint;
        }

        DrawTrajectory dt = new DrawTrajectory();
        dt.addPoint(point);
        double[] gradient;
        for (int i = 0; i < numberOfIterations; i++) {
            gradient = ihFunction.getGradient(point);
            if (validSolution(gradient)) {
                System.out.print("Final solution: ");
                printPoint(point);
                System.out.println("Number of needed iterations: " + i);

                dt.addPoint(point);
                System.out.println("Now drawing points...");
                dt.drawPoints();
                return point;
            }
            // In case that function is quadratic the solution will be found in O(1) using this
            //point = MatrixUtils.createColumnRealMatrix(point)
            //        .subtract(new LUDecomposition(ihFunction.getHesseMatrix(point)).getSolver().getInverse()
            //                .multiply(MatrixUtils.createColumnRealMatrix(gradient))).getColumn(0);

            //General approach for finding minimum

            RealMatrix direction = new LUDecomposition(ihFunction.getHesseMatrix(point)).getSolver().getInverse()
                    .multiply(MatrixUtils.createColumnRealMatrix(gradient));

            RealMatrix newPoint = MatrixUtils.createColumnRealMatrix(point)
                    .subtract(direction.scalarMultiply(Util.calculateLambda(ihFunction, point, direction.scalarMultiply(-1))));
            point = newPoint.getColumn(0);
            dt.addPoint(point);
            printPoint(point);
            if (i == numberOfIterations - 1) {
                return point;
            }
        }
        System.out.println("Unable to find solution.");
        return null;
    }

    private static void printPoint(double[] point) {
        for (int j = 0; j < point.length; j++) {
            double singleVariable = Math.round(point[j] * 10000) / 10000.0;
            System.out.print("x" + j + ": " + singleVariable + " ");
        }
        System.out.println();
    }

    private static boolean validSolution(double[] gradient) {
        for (int i = 0; i < gradient.length; i++) {
            if (Math.abs(gradient[i]) > ZERO) {
                return false;
            }
        }
        return true;
    }

}
