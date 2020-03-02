package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.*;
import hr.fer.zemris.trisat.model.VectorAndCorrection;
import hr.fer.zemris.trisat.util.Util;

import java.nio.file.Path;
import java.util.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ThirdAlgorithm {

    private static final int NUMBER_OF_ITERATIONS = 100000;

    public static BitVector execute(Path path) {
        SATFormula satFormula = Util.generateSATFormula(path);
        SATFormulaStats satFormulaStats = new SATFormulaStats(satFormula);
        // Možda random vrijednosti staviti
        BitVector oldAssignment = new BitVector(satFormula.getNumberOfVariables());
        List<VectorAndCorrection> bestVectors = new ArrayList<>();

        if (satFormula.isSatisfied(oldAssignment)) {
            System.out.println(oldAssignment);
            return oldAssignment;
        }

        // for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
        while (true) {
            satFormulaStats.setAssignment(oldAssignment, true);

            for (MutableBitVector currentAssignment : new BitVectorNGenerator(oldAssignment)) {

                satFormulaStats.setAssignment(currentAssignment, false);
                VectorAndCorrection vectorAndCorrection = new VectorAndCorrection(currentAssignment, satFormulaStats.getPercentageBonus());
                processCorrection(bestVectors, vectorAndCorrection);

            }

            oldAssignment = bestVectors.get((Math.abs(new Random().nextInt())) % bestVectors.size()).getVector();
            bestVectors.clear();

            if (satFormula.isSatisfied(oldAssignment)) {
                System.out.println(oldAssignment);
                return oldAssignment;
            }
        }

        //System.out.println("Reached maximum number of iterations.");
        //return null;
    }

    private static void processCorrection(List<VectorAndCorrection> vectorAndCorrections, VectorAndCorrection newVectorAndCorrection) {

        if (vectorAndCorrections.size() < SATFormulaStats.NUMBER_OF_BEST) {
            vectorAndCorrections.add(newVectorAndCorrection);
            return;
        }

        int indexOfLowestCorrection = -1;
        VectorAndCorrection currentLowestCorrection = vectorAndCorrections.get(0);
        for (int i = 0; i < vectorAndCorrections.size(); i++) {
            if (currentLowestCorrection.getCorrection() > vectorAndCorrections.get(i).getCorrection()) {
                currentLowestCorrection = vectorAndCorrections.get(i);
                indexOfLowestCorrection = i;
            }
        }

        if (indexOfLowestCorrection >= 0) {
            vectorAndCorrections.remove(indexOfLowestCorrection);
            vectorAndCorrections.add(newVectorAndCorrection);
        }
    }
}
