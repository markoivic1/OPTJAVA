package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ThreadBoundRNGProvider implements IRNGProvider {
    @Override
    public IRNG getRNG() {
        return ((IRNGProvider) Thread.currentThread()).getRNG();
    }
}
