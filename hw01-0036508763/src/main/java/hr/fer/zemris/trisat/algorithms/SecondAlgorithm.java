package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.*;
import hr.fer.zemris.trisat.util.Util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static hr.fer.zemris.trisat.util.Util.getFitness;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SecondAlgorithm {

    private static final int NUMBER_OF_ITERATIONS = 100000;

    public static BitVector execute(Path path) {
        SATFormula satFormula = Util.generateSATFormula(path);
        BitVector currentAssignment = new BitVector(new Random(), satFormula.getNumberOfVariables());
        int oldFitness = getFitness(satFormula, currentAssignment);

        if (satFormula.isSatisfied(currentAssignment)) {
            System.out.println(currentAssignment);
            return currentAssignment;
        }

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {


            List<BitVector> possibleCandidates = new ArrayList<>();

            for (BitVector neighbor : new BitVectorNGenerator(currentAssignment)) {
                int currentFitness = getFitness(satFormula, neighbor);
                if (currentFitness > oldFitness) {
                    oldFitness = currentFitness;
                    possibleCandidates.clear();
                    possibleCandidates.add(neighbor);
                } else if (currentFitness == oldFitness) {
                    possibleCandidates.add(neighbor);
                }
            }
            if (possibleCandidates.size() == 0) {
                System.out.println("Local optimum has been reached.");
                break;
            } else {
                currentAssignment = possibleCandidates.get((Math.abs(new Random().nextInt())) % possibleCandidates.size());
            }

            if (satFormula.isSatisfied(currentAssignment)) {
                System.out.println(currentAssignment);
                break;
            }
            if (i + 1 == NUMBER_OF_ITERATIONS) {
                System.out.println("Reached maximum number of iterations.");
            }
        }
        return currentAssignment;
    }
}
