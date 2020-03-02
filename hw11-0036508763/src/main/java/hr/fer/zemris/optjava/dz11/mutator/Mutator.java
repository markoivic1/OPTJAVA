package hr.fer.zemris.optjava.dz11.mutator;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface Mutator {
    void mutate(GASolution<int[]> child, GrayScaleImage template, double chance);
}
