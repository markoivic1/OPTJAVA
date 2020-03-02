package hr.fer.zemris.optjava.dz11.mutator;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GaussMutator implements Mutator {
    private int delta;

    public GaussMutator(int delta) {
        this.delta = delta;
    }

    public void mutate(GASolution<int[]> child, GrayScaleImage template, double chance) {
        IRNG rng = RNG.getRNG();
        if (rng.nextDouble() < chance) {
            child.getData()[0] += delta * rng.nextGaussian();
            child.getData()[0] += limit(child.getData()[0], 0, 255);
        }
        for (int i = 1; i < child.getData().length; i += 5) {
            if (rng.nextDouble() < chance) {
                child.getData()[i] += delta * rng.nextGaussian();
                child.getData()[i] = limit(child.getData()[i], 0, template.getWidth());
                if (child.getData()[i] > template.getWidth()) {
                    System.out.println();
                }
            }
            if (rng.nextDouble() < chance) {
                child.getData()[i + 1] += delta * rng.nextGaussian();
                child.getData()[i + 1] = limit(child.getData()[i + 1], 0, template.getHeight());
            }
            if (rng.nextDouble() < chance) {
                child.getData()[i + 2] += delta * rng.nextGaussian();
                child.getData()[i + 2] = limit(child.getData()[i + 2], 1, template.getWidth() - child.getData()[i]);
            }
            if (rng.nextDouble() < chance) {
                child.getData()[i + 3] += delta * rng.nextGaussian();
                child.getData()[i + 3] = limit(child.getData()[i + 3], 1, template.getHeight() - child.getData()[i + 1]);
            }
            if (rng.nextDouble() < chance) {
                child.getData()[i + 4] += delta * rng.nextGaussian();
                child.getData()[i + 4] = limit(child.getData()[i + 4], 0, 255);
            }
        }
    }

    private static int limit(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value >= max) {
            return max;
        }
        return value;
    }
}
