package hr.fer.zemris.optjava.dz2;

import hr.fer.zemris.optjava.dz2.algorithms.FirstA;
import hr.fer.zemris.optjava.dz2.algorithms.FirstB;
import hr.fer.zemris.optjava.dz2.algorithms.SecondA;
import hr.fer.zemris.optjava.dz2.algorithms.SecondB;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Jednostavno {

    private static final int ITERATIONS = 10000;
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 4) {
            System.out.println("Invalid number of arguments.");
            return;
        }
        double[] point = null;
        if (args.length == 4) {
            point = new double[2];
            point[0] = Double.parseDouble(args[2]);
            point[1] = Double.parseDouble(args[3]);
        }
        if (args[0].equals("1a")) {
            FirstA.execute(ITERATIONS, point);
        } else if (args[0].equals("1b")) {
            FirstB.execute(ITERATIONS, point);
        } else if (args[0].equals("2a")) {
            SecondA.execute(ITERATIONS, point);
        } else if (args[0].equals("2b")) {
            SecondB.execute(ITERATIONS, point);
        }
    }
}
