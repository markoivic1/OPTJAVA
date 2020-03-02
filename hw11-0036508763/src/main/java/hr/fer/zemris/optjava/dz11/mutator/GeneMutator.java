package hr.fer.zemris.optjava.dz11.mutator;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GeneMutator implements Mutator {
    public void mutate(GASolution<int[]> child, GrayScaleImage template, double chance) {
        IRNG rng = RNG.getRNG();
        if (rng.nextDouble() < chance) {
            child.getData()[0] = rng.nextInt(0, 255);
        }
        for (int i = 1; i < child.getData().length; i += 5) {
            if (rng.nextDouble() < chance) {
                child.getData()[i] = rng.nextInt(0, template.getWidth());
            }
            if (rng.nextDouble() < chance) {
                child.getData()[i + 1] = rng.nextInt(0, template.getHeight());
            }
            if (rng.nextDouble() < chance) {
                if (template.getWidth() - child.getData()[i] <= 1) {
                    child.getData()[i + 2] = 1;
                } else {
                    child.getData()[i + 2] = rng.nextInt(1, template.getWidth() - child.getData()[i]);
                }
            }
            if (rng.nextDouble() < chance) {
                if (template.getHeight() - child.getData()[i + 1] <= 1) {
                    child.getData()[i + 3] = 1;
                } else {
                    child.getData()[i + 3] = rng.nextInt(1, template.getHeight() - child.getData()[i + 1]);
                }
            }
            if (rng.nextDouble() < chance) {
                child.getData()[i + 4] = rng.nextInt(0, 255);
            }
        }
    }
}
