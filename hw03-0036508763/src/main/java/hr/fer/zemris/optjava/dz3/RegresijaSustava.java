package hr.fer.zemris.optjava.dz3;


import hr.fer.zemris.optjava.dz3.decoders.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoders.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.neighborhood.BitArrayNaturalNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayRandomUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solutions.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static java.lang.Math.pow;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class RegresijaSustava {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.print("Please enter path to a file describing predefined function ");
            System.out.println("and state which solution representation will be used (decimal/binary:xx)");
        }
        double[][] values;
        try {
            values = getValues(Paths.get(args[0]));
        } catch (IOException e) {
            System.out.println("Invalid file given.");
            return;
        }

        IFunction function = getFunction(values);
        if (args[1].equals("decimal")) {
            decimalAnnealing(function);
        } else if (args[1].startsWith("binary")) {
            int numberOfBits = Integer.parseInt(args[1].split(":")[1]);
            if (numberOfBits < 5 || numberOfBits > 30) {
                System.out.println("Please enter number of bits per variable in range from 5 to 30.");
                return;
            }
            binaryAnnealing(function, numberOfBits);
        } else {
            System.out.println("Unsupported solution representation.");
        }
    }

    private static void binaryAnnealing(IFunction function, int numberOfBits) {
        BitVectorSolution solution = new BitVectorSolution(6 * numberOfBits);
        solution.randomize(new Random());
        int[] range = new int[6];
        Arrays.fill(range, numberOfBits);
        BitArrayNaturalNeighborhood neighborhood = new BitArrayNaturalNeighborhood(range);
        IOptAlgorithm<BitVectorSolution> algorithm = new SimulatedAnnealing<>(
                new NaturalBinaryDecoder(-3.5, 7.5, numberOfBits, 6),
                neighborhood,
                solution,
                function,
                true,
                new GeometricTempSchedule(0.95, 1500, 10000, 300));
        algorithm.run();
    }

    private static double[][] getValues(Path path) throws IOException{
        List<String> lines;

        lines = Files.readAllLines(path);

        lines = lines.stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

        double[][] values = new double[20][6];
        int i = 0;
        for (String s : lines) {
            String[] stringValues = s.substring(1, s.length() - 2).split(", ");
            for (int j = 0; j < stringValues.length; j++) {
                values[i][j] = Double.parseDouble(stringValues[j]);
            }
            i++;
        }
        return values;
    }

    private static IFunction getFunction(double[][] values) {
        return array -> {
                double sum = 0;
                for (int j = 0; j < 20; j++) {
                    double[] data = values[j];
                    double currentSum = 0;
                    currentSum += array[0] * data[0];
                    currentSum += array[1] * pow(data[0], 3) * data[1];
                    currentSum += array[2] * exp(array[3] * data[2]) * (1 + cos(array[4] * data[3]));
                    currentSum += array[5] * data[3] * pow(data[4], 2);
                    currentSum -= data[5];
                    sum += currentSum * currentSum;
                }
                return sum;
            };
    }

    private static void decimalAnnealing(IFunction function) {
        DoubleArraySolution solution = new DoubleArraySolution(6);

        solution.randomize(new Random(), new double[]{-7,-7,-7,-7,-7,-7}, new double[]{7,7,7,7,7,7}); // First solution range numbers that algorithm worked for
        //solution.randomize(new Random(), new double[]{-10,-10,-10,-10,-10,-10}, new double[]{10,10,10,10,10,10});
        INeighborhood<DoubleArraySolution> doubleNeighborhood = new DoubleArrayRandomUnifNeighborhood(new double[]{-0.1, 0.1});
        IOptAlgorithm<DoubleArraySolution> algorithm = new SimulatedAnnealing<>(
                new PassThroughDecoder(),
                doubleNeighborhood,
                solution,
                function,
                true,
                //new GeometricTempSchedule(0.95, 1000, 10000, 200), // First parameters that algorithm worked for
                new GeometricTempSchedule(0.95, 1500, 10000, 300));

        algorithm.run();
    }
}
