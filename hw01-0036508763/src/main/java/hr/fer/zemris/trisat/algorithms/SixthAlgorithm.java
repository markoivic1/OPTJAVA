package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.util.Util;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SixthAlgorithm {

    private static final double PERCENTAGE = 0.2;
    private static final int MAX_FLIPS = 100_000;
    private static final int MAX_CYCLES = 100;

    public static BitVector execute(Path path) {
        SATFormula satFormula = Util.generateSATFormula(path);
        BitVector assignment = new BitVector(new Random(), satFormula.getNumberOfVariables());

        assignment = new BitVector(new Random(), satFormula.getNumberOfVariables());
        Set<String> invalidAssignments = new HashSet<>();
        int oldSize = 0;
        int cycles = 0;

        //for (int change = 0; change < MAX_FLIPS; change++) {
        while (true) {
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

            bestCandidates.forEach(v -> invalidAssignments.add(v.toString()));

            int newSize = invalidAssignments.size();

            if (newSize == oldSize) {
                cycles++;
                if (cycles == MAX_CYCLES) {
                    assignment = randomlyChangeVariables(assignment);
                    invalidAssignments.clear();
                    cycles = 0;
                    continue;
                }
            }
            assignment = bestCandidates.get(Math.abs((new Random()).nextInt()) % bestCandidates.size());
            oldSize = newSize;
        }

        //System.out.println("Unable to find results.");
        //return null;
    }

    private static BitVector randomlyChangeVariables(BitVector assignment) {
        int numberOfChanges = (int) Math.ceil(PERCENTAGE * assignment.getSize());
        MutableBitVector mutableBitVector = assignment.copy();
        for (int i = 0; i < numberOfChanges; i++) {
            int index = Math.abs(new Random().nextInt()) % assignment.getSize();
            mutableBitVector.set(index, !assignment.get(index));
        }
        return mutableBitVector;
    }
}
