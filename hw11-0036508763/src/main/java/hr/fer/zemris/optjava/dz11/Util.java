package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.generic.ga.GASolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static void saveData(GASolution<int[]> solution, String optimalParametersPath) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < solution.getData().length; i++) {
            sb.append(solution.getData()[i]).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        try {
            Files.write(Paths.get(optimalParametersPath), sb.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Unable to write to a file.");
        }
    }
}
