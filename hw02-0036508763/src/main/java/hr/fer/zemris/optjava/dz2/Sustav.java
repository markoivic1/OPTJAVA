package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Sustav {

    public static void gradientDescent(int numberOfIterations, double[][] values, List<Double> functionValues) {
        List<IFunction> functions = createFunctions(values, functionValues);

        IFunction function = getHesseFunction(values, functionValues, functions);

        double[] point = NumOptAlgorithms.gradientDescent(function, numberOfIterations, null);
        System.out.print("Deviation: " + function.getValue(point));
    }

    public static void newtonsMethod(int numberOfIterations, double[][] values, List<Double> functionValues) {
        List<IFunction> functions = createFunctions(values, functionValues);

        IHFunction function = getHesseFunction(values, functionValues, functions);

        double[] point = NumOptAlgorithms.newtonsMethod(function, numberOfIterations, null);
        System.out.print("Deviation: " + function.getValue(point));

    }

    private static IHFunction getHesseFunction(double[][] values, List<Double> functionValues, List<IFunction> functions) {
        return new IHFunction() {
                @Override
                public RealMatrix getHesseMatrix(double[] point) {
                    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[10][10]);
                    for (int k = 0; k < 10; k++) {
                        RealMatrix currentMatrix = MatrixUtils.createRealMatrix(new double[10][10]);
                        double[] currentValue = values[k];
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                currentMatrix.setEntry(i, j, 2 * currentValue[i] * currentValue[j]);
                            }
                        }
                        matrix = matrix.add(currentMatrix);
                    }
                    return matrix;
                }

                @Override
                public int getNumberOfVariables() {
                    return 10;
                }

                @Override
                public double getValue(double[] point) {
                    double sum = 0;
                    for (int i = 0; i < functions.size(); i++) {
                        sum += Math.pow(functions.get(i).getValue(point) - functionValues.get(i), 2);
                    }
                    return sum;
                }

                @Override
                public double[] getGradient(double[] point) {
                    RealMatrix gradient = MatrixUtils.createColumnRealMatrix(new double[10]);
                    for (int i = 0; i < functions.size(); i++) {
                        gradient = gradient.add(MatrixUtils.createColumnRealMatrix(functions.get(i).getGradient(point)));
                    }
                    return gradient.getColumn(0);
                }
            };
    }

    public static List<IFunction> createFunctions(double[][] values, List<Double> functionValues) {
        List<IFunction> functions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            double[] data = values[i];
            double functionValue = functionValues.get(i);
            IFunction function = new IFunction() {
                @Override
                public int getNumberOfVariables() {
                    return 10;
                }

                @Override
                public double getValue(double[] point) {
                    double sum = 0;
                    for (int i = 0; i < 10; i++) {
                        sum += data[i] * point[i];
                    }
                    return sum;
                }

                @Override
                public double[] getGradient(double[] point) {
                    double[] gradient = new double[10];
                    for (int j = 0; j < functions.size(); j++) {
                        double gradientValue;
                        // 2*a*e
                        gradientValue = 2 * data[j] * (getValue(point) - functionValue);
                        gradient[j] = gradientValue;
                    }
                    return gradient;
                }
            };
            functions.add(function);
        }
        return functions;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }
        List<Double> functionValues = new ArrayList<>();
        List<String> data;
        data = Util.getData(args[2]);
        if (data == null) return;
        double[][] values = Util.getValues(functionValues, data);
        if (args[0].equals("grad")) {
            gradientDescent(Integer.parseInt(args[1]), values, functionValues);
        } else if (args[0].equals("newton")) {
            newtonsMethod(Integer.parseInt(args[1]), values, functionValues);
        }
    }


}
