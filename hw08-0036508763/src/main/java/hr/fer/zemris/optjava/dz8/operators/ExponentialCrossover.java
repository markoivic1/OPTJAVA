package hr.fer.zemris.optjava.dz8.operators;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ExponentialCrossover implements Crossover {
    @Override
    public double[] cross(double[] target, double[] mutant, double chance, Random random) {
        double[] trial = new double[target.length];
        int startingIndex = random.nextInt(target.length);
        int index;
        boolean inRow = true;
        int counter = 0;
        while (counter != trial.length) {
            index = (startingIndex + counter) % trial.length;
            boolean takeFromMutant = random.nextDouble() < chance;
            if (takeFromMutant == false) {
                inRow = false;
            }
            if (inRow) {
                trial[index] = mutant[index];
            } else {
                trial[index] = target[index];
            }
            counter++;
        }
        return trial;
    }
}
