package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.util.Util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class FourthAlgorithm {

    private static final int MAX_TRIES = 100;
    private static final int MAX_FLIPS = 100_000;

    public static BitVector execute(Path path) {
        SATFormula satFormula = Util.generateSATFormula(path);

        BitVector assignment = null;
        for (int restart = 0; restart < MAX_TRIES; restart++) {
            assignment = new BitVector(new Random(), satFormula.getNumberOfVariables());
            for (int change = 0; change < MAX_FLIPS; change++) {
                if (satFormula.isSatisfied(assignment)) {
                    System.out.println(assignment);
                    return assignment;
                }
                int fitness = 0;
                List<BitVector> bestCandidates = new ArrayList<>();

                for (BitVector neighbor : new BitVectorNGenerator(assignment)) {
                    int currentFitness = Util.getFitness(satFormula, neighbor);
                    if (currentFitness > fitness) {
                        fitness = currentFitness;
                        bestCandidates.clear();
                        bestCandidates.add(neighbor);
                    } else if (currentFitness == fitness) {
                        bestCandidates.add(neighbor);
                    }
                }

                assignment = bestCandidates.get(Math.abs((new Random()).nextInt()) % bestCandidates.size());
            }
        }
        System.out.println("Result could be found.");
        return null;
    }
}
