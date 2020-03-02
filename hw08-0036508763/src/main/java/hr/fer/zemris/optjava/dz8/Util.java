package hr.fer.zemris.optjava.dz8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static IReadOnlyDataset loadData(String path, int samples) {
        List<Double> values;
        try {
            values = Files.readAllLines(Paths.get(path)).stream().map(s -> Double.parseDouble(s.trim())).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Invalid path was given.");
            return null;
        }
        if (samples == -1) {
            samples = values.size();
        }
        double max = values.stream().max(Comparator.comparingDouble(Double::doubleValue)).get();
        double min = values.stream().min(Comparator.comparingDouble(Double::doubleValue)).get();
        values = values.stream().map(i -> -1 + 2 * (i - min) / (max - min)).collect(Collectors.toList());
        double[] valueArray = new double[samples];
        for (int i = 0; i < valueArray.length; i++) {
            valueArray[i] = values.get(i);
        }
        return new IReadOnlyDataset() {
            @Override
            public int getSize() {
                return valueArray.length;
            }

            @Override
            public double getSample(int index) {
                return valueArray[index];
            }
        };
    }
}
