package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.util.Util;

import java.nio.file.Path;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class FifthAlgorithm {
    private static final int MAX_TRIES = 1000;
    private static final int MAX_FLIPS = 100_000;
    private static final double PROBABILITY = 0.2;

    public static BitVector execute(Path path) {
        SATFormula satFormula = Util.generateSATFormula(path);

        for (int restart = 0; restart < MAX_TRIES; restart++) {
            BitVector assignment = new BitVector(new Random(), satFormula.getNumberOfVariables());

            for (int change = 0; change < MAX_FLIPS; change++) {
                if (satFormula.isSatisfied(assignment)) {
                    System.out.println(assignment);
                    return assignment;
                }

                while (true) {
                    int randomClauseIndex = Math.abs(new Random().nextInt()) % satFormula.getNumberOfClauses();
                    if (satFormula.getClause(randomClauseIndex).isSatisfied(assignment)) {
                        break;
                    }
                    MutableBitVector mutableBitVector = assignment.copy();
                    if (new Random().nextDouble() < PROBABILITY) {
                        int literal = Math.abs(satFormula.getClause(randomClauseIndex).getLiteral(
                                Math.abs(new Random().nextInt())
                                        % satFormula.getClause(randomClauseIndex).getSize()));
                        mutableBitVector.set(literal - 1, !mutableBitVector.get(literal - 1));
                        assignment = mutableBitVector;
                    }
                    if (new Random().nextDouble() < 1 - PROBABILITY) {
                        int fitness = 0;
                        int literal;
                        int bestLiteral = -1;
                        for (int i = 0; i < satFormula.getClause(randomClauseIndex).getSize(); i++) {
                            literal = Math.abs(satFormula.getClause(randomClauseIndex).getLiteral(i));
                            mutableBitVector.set(literal - 1, !mutableBitVector.get(literal - 1));
                            if (Util.getFitness(satFormula, mutableBitVector) > fitness) {
                                fitness = Util.getFitness(satFormula, mutableBitVector);
                                bestLiteral = literal;
                            }
                            mutableBitVector.set(literal - 1, !mutableBitVector.get(literal - 1));
                        }

                        mutableBitVector.set(bestLiteral - 1, !mutableBitVector.get(bestLiteral - 1));
                        assignment = mutableBitVector;
                    }
                }
            }
        }

        System.out.println("Unable to find results.");
        return null;
    }
}
