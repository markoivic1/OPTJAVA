package hr.fer.zemris.optjava.dz8.operators;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class UniformCrossover implements Crossover {
    @Override
    public double[] cross(double[] target, double[] mutant, double chance, Random random) {
        double[] trial = new double[target.length];
        int guaranteed = random.nextInt(target.length);
        for (int i = 0; i < target.length; i++) {
            if (guaranteed == i) {
                trial[i] = mutant[i];
                continue;
            }
            trial[i] = chance > random.nextDouble() ? mutant[i] : target[i];
        }
        return trial;
    }
}
