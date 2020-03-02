package hr.fer.zemris.trisat.algorithms;

import java.nio.file.Paths;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TriSatSolver {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments");
            return;
        }
        if (args[0].equals("1")) {
            FirstAlgorithm.execute(Paths.get(args[1]));
        } else if (args[0].equals("2")) {
            SecondAlgorithm.execute(Paths.get(args[1]));
        } else if (args[0].equals("3")) {
            ThirdAlgorithm.execute(Paths.get(args[1]));
        } else if (args[0].equals("4")) {
            FourthAlgorithm.execute(Paths.get(args[1]));
        } else if (args[0].equals("5")) {
            FifthAlgorithm.execute(Paths.get(args[1]));
        } else if (args[0].equals("6")) {
            SixthAlgorithm.execute(Paths.get(args[1]));
        } else {
            System.out.println("Invalid algorithm number.");
        }
    }
}
