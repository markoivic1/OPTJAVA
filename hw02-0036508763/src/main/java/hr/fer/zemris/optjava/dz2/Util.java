package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    private static final double ZERO = 0.0001;

    public static double calculateLambda(IFunction function, double[] point, RealMatrix direction) {
        int maxIterations = 10000;
        double lambdaLower = 0;
        double lambdaUpper = 0.01;

        while (true) {
            double expression = getExpression(function, lambdaUpper, point, direction);
            if (expression > 0) {
                break;
            }
            lambdaUpper *= 2;
        }
        double lambda = (lambdaLower + lambdaUpper) / 2;
        for (int i = 0; i < maxIterations; i++) {
            lambda = (lambdaLower + lambdaUpper) / 2;
            double expression = getExpression(function, lambda, point, direction);
            if (Math.abs(expression) < ZERO) {
                return lambda;
            } else if (expression > 0) {
                lambdaUpper = lambda;
            } else if (expression < 0) {
                lambdaLower = lambda;
            }
            if (i == maxIterations - 1) {
                System.out.println(expression);
            }
        }
        return lambda;
    }

    public static List<String> getData(String arg) {
        List<String> data;
        try {
            data = Files.readAllLines(Paths.get(arg)).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Unable to load file.");
            return null;
        }
        return data;
    }

    public static double[][] getValues(List<Double> functionValues, List<String> data) {
        double[][] values = new double[data.size()][data.size()];
        int i = 0;
        for (String s : data) {
            String[] stringValues = s.substring(1, s.length() - 2).split(", ");
            for (int j = 0; j < stringValues.length - 1; j++) {
                values[i][j] = Double.parseDouble(stringValues[j]);
            }
            i++;
            functionValues.add(Double.parseDouble(stringValues[stringValues.length - 1]));
        }
        return values;
    }

    public static double getExpression(IFunction function, double lambda, double[] point, RealMatrix direction) {
        RealMatrix gradient = MatrixUtils.createColumnRealMatrix(function.getGradient(
            MatrixUtils.createColumnRealMatrix(point)
                    .add(direction.scalarMultiply(lambda)).getColumn(0))).transpose();
        return gradient.multiply(direction).getEntry(0, 0);
    }

    public static double generateNumberBetween(double first, double second) {
        return (new Random().nextDouble()) * (second - first) + first;
    }
}
