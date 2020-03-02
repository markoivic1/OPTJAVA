package hr.fer.zemris.optjava.dz6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static double[][] getNiWeightMatrix(Path path, double beta) throws IOException {
        return getNiMatrix(getDistances(path), beta);
    }

    public static double[][] getDistances(Path path) throws IOException {
        List<String> data = Files.readAllLines(path);
        String type = extractField(data, "EDGE_WEIGHT_TYPE");
        return switch (type) {
            case "ATT" -> parseEuc2D(data);
            case "EXPLICIT" -> parseExplicit(data);
            default -> parseEuc2D(data);
        };
    }


    private static double[][] getNiMatrix(double[][] matrix, double beta) {
        double[][] niMatrix = matrix.clone();
        for (int i = 0; i < niMatrix.length; i++) {
            for (int j = i; j < niMatrix.length; j++) {
                niMatrix[i][j] = pow(1 / niMatrix[i][j], beta);
                niMatrix[j][i] = niMatrix[i][j];
            }
        }
        return niMatrix;
    }

    private static double[][] getDistance(Map<Integer, Tuple> tupleMap) {
        double[][] matrix = new double[tupleMap.size()][tupleMap.size()];
        for (int i = 0; i < tupleMap.size(); i++) {
            for (int j = i; j < tupleMap.size(); j++) {
                Tuple first = tupleMap.get(i);
                Tuple second = tupleMap.get(j);
                matrix[i][j] = sqrt(pow(first.getX() - second.getX(), 2) + pow(first.getY() - second.getY(), 2));
                matrix[j][i] = matrix[i][j];
            }
        }
        return matrix;
    }

    /*private static double[][] getNiMatrix(Map<Integer, Tuple> tupleMap, double beta) {
        double[][] matrix = new double[tupleMap.size()][tupleMap.size()];
        for (int i = 0; i < tupleMap.size(); i++) {
            for (int j = i; j < tupleMap.size(); j++) {
                Tuple first = tupleMap.get(i);
                Tuple second = tupleMap.get(j);
                double distance = sqrt(pow(first.getX() - second.getX(), 2) + pow(first.getY() - second.getY(), 2));
                matrix[i][j] = pow(1 / distance, beta);
                matrix[j][i] = matrix[i][j];
            }
        }
        return matrix;
    }*/


    private static double[][] parseExplicit(List<String> data) {
        int size = Integer.parseInt(extractField(data, "DIMENSION"));
        double[][] matrix = new double[size][size];
        int i = 0;
        while (!data.get(i).startsWith("EDGE_WEIGHT_SECTION")) {
            i++;
        }
        i++;
        int currentLine = 0;
        while (!data.get(i).startsWith("DISPLAY_DATA_SECTION")) {
            matrix[currentLine] = parseExplicitLine(data.get(i));
            i++;
            currentLine++;
        }
        return matrix;
    }

    private static double[] parseExplicitLine(String line) {
        String[] stringValues = line.trim().split(" +");
        double[] doubleValues = new double[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            doubleValues[i] = Double.parseDouble(stringValues[i]);
        }
        return doubleValues;
    }

    private static double[][] parseEuc2D(List<String> data) {
        int i = 0;
        while (!data.get(i).startsWith("NODE_COORD_SECTION")) {
            i++;
        }
        i++;
        int currentLine = 0;
        Map<Integer, Tuple> map = new HashMap<>();
        while (!data.get(i).startsWith("EOF")) {
            map.put(currentLine, parseEucLine(data.get(i)));
            i++;
            currentLine++;
        }
        return getDistance(map);
    }


    private static Tuple parseEucLine(String lineAsString) {
        String[] stringNumbers = lineAsString.split(" +");
        double[] line = new double[stringNumbers.length - 1];
        for (int i = 0; i < stringNumbers.length - 1; i++) {
            line[i] = Double.parseDouble(stringNumbers[i + 1]);
        }
        return new Tuple(line);
    }

    private static String extractField(List<String> data, String field) {
        int i = 0;
        while (!data.get(i).startsWith(field)) {
            i++;
        }
        int indexOfColumn = data.get(i).indexOf(':');
        return data.get(i).substring(indexOfColumn + 1).trim();
    }
}
