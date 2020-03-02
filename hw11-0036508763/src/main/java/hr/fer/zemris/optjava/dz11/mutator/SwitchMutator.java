package hr.fer.zemris.optjava.dz11.mutator;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SwitchMutator implements Mutator {
    private IRNG rng = RNG.getRNG();
    private int minSwitches;
    private int maxswitches;

    public SwitchMutator(int minSwitches, int maxswitches) {
        this.minSwitches = minSwitches;
        this.maxswitches = maxswitches;
    }

    @Override
    public void mutate(GASolution<int[]> child, GrayScaleImage template, double chance) {
        int switches = rng.nextInt(minSwitches, maxswitches);
        for (int i = 0; i < switches; i++) {
            int first = ((rng.nextInt(1, child.getData().length) - 1) / 5) * 5 + 1;
            int second = ((rng.nextInt(1, child.getData().length) - 1) / 5) * 5 + 1;

            int[] tmp = new int[5];
            for (int j = 0; j < tmp.length; j++) {
                tmp[j] = child.getData()[first + j];
            }

            for (int j = 0; j < tmp.length; j++) {
                child.getData()[first + j] = child.getData()[second + j];
            }

            for (int j = 0; j < tmp.length; j++) {
                child.getData()[second + j] = tmp[j];
            }

            //switch color
            if (rng.nextBoolean()) {
                child.getData()[first + 4] = rng.nextInt(0, 255);
            } else {
                child.getData()[second + 4] = rng.nextInt(0, 255);
            }
        }
    }
}
