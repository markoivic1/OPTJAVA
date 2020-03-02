package hr.fer.zemris.optjava.dz7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static final Random random = new Random();
    public static IReadOnlyDataset loadData(String path) {
        String[] data;
        try {
            data = Files.readString(Paths.get(path)).split("\n");
        } catch (IOException e) {
            System.out.println("Invalid input");
            return null;
        }
        double[][][] samples = new double[data.length][2][0];
        for (int i = 0; i < data.length; i++) {
            String inputString = data[i].split(":")[0];
            createSamples(samples, i, inputString, 0);

            String outputString = data[i].split(":")[1];
            createSamples(samples, i, outputString, 1);
        }
        return new IReadOnlyDataset() {
            public int getSize() {
                return samples.length;
            }

            public int getInputSize() {
                return samples[0][0].length;
            }

            public int getOutputSize() {
                return samples[0][1].length;
            }

            public double[] getSampleInput(int index) {
                return samples[index][0];
            }

            public double[] getSampleOutput(int index) {
                return samples[index][1];
            }
        };
    }

    private static void createSamples(double[][][] samples, int i, String data, int index) {
        String[] inputData = data.substring(1, data.length() - 1).split(",");
        double[] inputs = new double[inputData.length];
        for (int j = 0; j < inputData.length; j++) {
            inputs[j] = Double.parseDouble(inputData[j]);
        }
        samples[i][index] = inputs;
    }

    public static void printComparison(FFANN ffann, IReadOnlyDataset dataset, double[] weights) {
        int validResults = 0;
        for (int i = 0; i < dataset.getSize(); i++) {
            double[] currentResults = new double[dataset.getOutputSize()];
            ffann.calcOutputs(dataset.getSampleInput(i), weights, currentResults);
            double[] expectedResults = dataset.getSampleOutput(i);
            StringBuilder sb = new StringBuilder();
            fillWithResults(currentResults, sb);
            sb.append(':');
            fillWithResults(expectedResults, sb);
            String values = sb.toString();
            if (values.substring(0, values.indexOf(':')).equals(values.substring(values.indexOf(":") + 1))) {
                validResults++;
            }
            System.out.println(values);
        }
        System.out.println(validResults + "/" + dataset.getSize());
    }

    private static void fillWithResults(double[] currentResults, StringBuilder sb) {
        sb.append("(");
        for (int j = 0; j < currentResults.length; j++) {
            if (currentResults[j] < 0.5) {
                sb.append(0).append(",");
            } else {
                sb.append(1).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
    }

    public static double randomInRange(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    public static void mutate(double[] solution, double mi, double sigma, double chance) {
        for (int j = 0; j < solution.length; j++) {
            if (random.nextDouble() < chance) {
                solution[j] += ((random.nextGaussian() - 0.5) + mi) * sigma;
            }
        }
    }
}
