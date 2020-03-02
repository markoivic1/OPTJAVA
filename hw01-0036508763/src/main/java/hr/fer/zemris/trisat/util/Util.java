package hr.fer.zemris.trisat.util;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.Clause;
import hr.fer.zemris.trisat.SATFormula;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static List<String> parseCNF(Path path) {
        List<String> lines = new LinkedList<>();
        try {
            lines = Files.readAllLines(path).stream().filter(s -> !s.startsWith("c")).collect(Collectors.toList());
            int index = 0;
            for (String line : lines) {
                if (line.startsWith("%")) {
                    while (index != lines.size()) {
                        lines.remove(index);
                    }
                    break;
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println(path.toAbsolutePath() + " could not be opened.");
        }
        return lines;
    }

    public static SATFormula generateSATFormula(Path path) {

        List<String> data = parseCNF(path);

        int numberOfClauses = 0;
        int numberOfVariables = 0;
        for (String line : data) {
            if (line.startsWith("p")) {
                numberOfVariables = Integer.parseInt(line.split(" +")[2]);
                numberOfClauses = Integer.parseInt(line.split(" +")[3]);
                break;
            }
        }
        Clause[] clauses = new Clause[numberOfClauses];
        int index = 0;
        for (String line : data) {
            if (line.startsWith("p")) {
                continue;
            }
            List<Integer> indexList = new ArrayList<>();
            String[] splitLine = line.trim().split(" +");
            for (int i = 0; i < numberOfVariables; i++) {
                if (splitLine[i].equals("0")) {
                    break;
                }
                indexList.add(Integer.parseInt(splitLine[i]));
            }
            clauses[index] = new Clause(indexList.stream().mapToInt(Integer::intValue).toArray());
            index++;
        }
        return new SATFormula(numberOfVariables, clauses);
    }

    public static boolean[] convertIntToBool(int number, int size) {
        boolean[] vector = new boolean[size];
        String stringVectors = Integer.toBinaryString(number);
        while (stringVectors.length() < size) {
            stringVectors = "0" + stringVectors;
        }
        for (int i = 0; i < size; i++) {
            vector[i] = stringVectors.charAt(i) == '1';
        }
        return vector;
    }

    public static int getFitness(SATFormula formula, BitVector assignment) {
        int currentFitness = 0;
        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            if (formula.getClause(i).isSatisfied(assignment)) {
                currentFitness++;
            }
        }
        return currentFitness;
    }
}
