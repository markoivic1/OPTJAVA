package hr.fer.zemris.trisat.algorithms;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.util.Util;

import java.nio.file.Path;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class FirstAlgorithm {

    public static BitVector execute(Path path) {
        SATFormula satFormula = Util.generateSATFormula(path);
        int count = 0;
        int size = satFormula.getNumberOfVariables();
        boolean foundFirst = false;
        BitVector first = null;
        while (true) {
            BitVector currentBitVector = new BitVector(Util.convertIntToBool(count, size));
            if (satFormula.isSatisfied(currentBitVector)) {
                if (!foundFirst) {
                    first = currentBitVector;
                    System.out.println(first);
                    foundFirst = true;
                } else {
                    System.out.println(currentBitVector);
                }
            }
            count++;
            if (currentBitVector.toString().equals("1".repeat(size))) {
                break;
            }
        }
        return first;
    }
}
