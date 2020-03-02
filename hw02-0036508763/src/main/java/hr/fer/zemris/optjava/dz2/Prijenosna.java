package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Prijenosna {
    private static void gradientDescent(int numberOfIterations, double[][] values, List<Double> functionValues) {
        List<IFunction> functions = createFunctions(values, functionValues);
        IFunction function = getFunction(functionValues, functions);

        double[] point = NumOptAlgorithms.gradientDescent(function, numberOfIterations, null);
        System.out.print("Deviation: " + function.getValue(point));
    }

    private static IFunction getFunction(List<Double> functionValues, List<IFunction> functions) {
        return new IHFunction() {

            @Override
            public RealMatrix getHesseMatrix(double[] point) {
                RealMatrix hesse = MatrixUtils.createRealMatrix(new double[6][6]);
                for (int i = 0; i < functions.size(); i++) {
                    hesse = hesse.add(((IHFunction) functions.get(i)).getHesseMatrix(point));
                }
                return hesse;
            }

            @Override
                public int getNumberOfVariables() {
                    return 6;
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
                    RealMatrix gradient = MatrixUtils.createColumnRealMatrix(new double[6]);
                    for (int i = 0; i < functions.size(); i++) {
                        gradient = gradient.add(MatrixUtils.createColumnRealMatrix(functions.get(i).getGradient(point)));
                    }
                    return MatrixUtils.createRealVector(gradient.getColumn(0)).unitVector().toArray();
                }
            };
    }

    private static void newtonsMethod(int numberOfIterations, double[][] values, List<Double> functionValues) {
        List<IFunction> functions = createFunctions(values, functionValues);
        IHFunction function = (IHFunction) getFunction(functionValues, functions);

        double[] point = NumOptAlgorithms.newtonsMethod(function, numberOfIterations, null);
        System.out.print("Deviation: " + function.getValue(point));
    }

    private static List<IFunction> createFunctions(double[][] values, List<Double> functionValues) {
        List<IFunction> functions = new ArrayList<>();
        for (int i = 0; i < functionValues.size(); i++) {
            double[] data = values[i];
            double functionValue = functionValues.get(i);
            IHFunction function = new IHFunction() {
                @Override
                public RealMatrix getHesseMatrix(double[] point) {
                    // derivative
                    double aFactor = data[0];
                    double bFactor = pow(data[0], 3) * data[1];
                    double cFactor = exp(point[3] * data[2]) * (1 + cos(point[4] * data[3]));
                    double dFactor = point[2] * data[2] * exp(point[3] * data[2]) * (1 + cos(point[4] * data[3]));
                    double eFactor = -1 * data[3] * point[2] * exp(point[3] * data[2]) * sin(point[4] * data[3]);
                    double fFactor = data[3] * pow(data[4], 2);

                    double[][] matrix = new double[6][6];
                    double e = (getValue(point) - functionValue);

                    matrix[0][0] = aFactor * aFactor;
                    matrix[0][1] = aFactor * bFactor;
                    matrix[0][2] = aFactor * cFactor;
                    matrix[0][3] = aFactor * dFactor;
                    matrix[0][4] = aFactor * eFactor;
                    matrix[0][5] = aFactor * fFactor;

                    matrix[1][0] = bFactor * aFactor;
                    matrix[1][1] = bFactor * bFactor;
                    matrix[1][2] = bFactor * cFactor;
                    matrix[1][3] = bFactor * dFactor;
                    matrix[1][4] = bFactor * eFactor;
                    matrix[1][5] = bFactor * fFactor;

                    matrix[2][0] = cFactor * aFactor;
                    matrix[2][1] = cFactor * bFactor;
                    matrix[2][2] = cFactor * cFactor;
                    matrix[2][3] = cFactor * dFactor + e * data[2] * cFactor;
                    matrix[2][4] = cFactor * eFactor + e * data[3] * exp(point[3] * data[2]) * (-1) * sin(point[4] * data[3]);
                    matrix[2][5] = cFactor * fFactor;

                    matrix[3][0] = dFactor * aFactor;
                    matrix[3][1] = dFactor * bFactor;
                    matrix[3][2] = dFactor * cFactor + e * cFactor * data[2];
                    matrix[3][3] = dFactor * dFactor + e * dFactor * data[2];
                    matrix[3][4] = dFactor * eFactor + e * point[2] * data[2] * data[3] * exp(point[3] * data[2]) * (-1) * sin(point[4] * data[3]);
                    matrix[3][5] = dFactor * fFactor;

                    matrix[4][0] = eFactor * aFactor;
                    matrix[4][1] = eFactor * bFactor;
                    matrix[4][2] = eFactor * cFactor + e * (-1) * data[3] * exp(point[3] * data[2]) * sin(point[4] * data[3]);
                    matrix[4][3] = eFactor * dFactor + e * data[2] * dFactor;
                    matrix[4][4] = eFactor * eFactor + e * point[2] * exp(point[3] * data[2]) * (-1) * data[3] * cos(point[4] * data[3]);
                    matrix[4][5] = eFactor * fFactor;

                    matrix[5][0] = fFactor * aFactor;
                    matrix[5][1] = fFactor * bFactor;
                    matrix[5][2] = fFactor * cFactor;
                    matrix[5][3] = fFactor * dFactor;
                    matrix[5][4] = fFactor * eFactor;
                    matrix[5][5] = fFactor * fFactor;

                    return MatrixUtils.createRealMatrix(matrix).scalarMultiply(2);
                }

                @Override
                public int getNumberOfVariables() {
                    return 6;
                }

                @Override
                public double getValue(double[] point) {
                    double sum = 0;
                    sum += point[0] * data[0];
                    sum += point[1] * pow(data[0], 3) * data[1];
                    sum += point[2] * exp(point[3] * data[2]) * (1 + cos(point[4] * data[3]));
                    sum += point[5] * data[3] * pow(data[4], 2);
                    return sum;
                }

                @Override
                public double[] getGradient(double[] point) {
                    double[] gradient = new double[6];

                    gradient[0] = data[0];
                    gradient[1] = pow(data[0], 3) * data[1];
                    gradient[2] = exp(point[3] * data[2]) * (1 + cos(point[4] * data[3]));
                    gradient[3] = point[2] * data[2] * exp(point[3] * data[2]) * (1 + cos(point[4] * data[3]));
                    gradient[4] = -1 * data[3] * point[2] * exp(point[3] * data[2]) * sin(point[4] * data[3]);
                    gradient[5] = data[3] * pow(data[4], 2);

                    double factor = 2 * (getValue(point) - functionValue);
                    for (int j = 0; j < gradient.length; j++) {
                        gradient[j] *= factor;
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
        List<String> data = Util.getData(args[2]);
        if (data == null) return;
        double[][] values = getValues(functionValues, data);
        if (args[0].equals("grad")) {
            gradientDescent(Integer.parseInt(args[1]), values, functionValues);
        } else if (args[0].equals("newton")) {
            newtonsMethod(Integer.parseInt(args[1]), values, functionValues);
        }
    }

    private static double[][] getValues(List<Double> functionValues, List<String> data) {
        double[][] values = new double[20][5];
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
}
