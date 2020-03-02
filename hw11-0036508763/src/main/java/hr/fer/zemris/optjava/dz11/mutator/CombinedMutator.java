package hr.fer.zemris.optjava.dz11.mutator;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.List;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class CombinedMutator implements Mutator {
    private List<Mutator> mutators;
    private static IRNG rng = RNG.getRNG();
    public CombinedMutator(List<Mutator> mutators) {
        this.mutators = mutators;
    }

    @Override
    public void mutate(GASolution<int[]> child, GrayScaleImage template, double chance) {
        int index = rng.nextInt(0, mutators.size());
        mutators.get(index).mutate(child, template, chance);
    }
}
