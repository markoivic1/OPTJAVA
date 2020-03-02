package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.optjava.rng.EVOThread;

import java.util.concurrent.ThreadFactory;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class EvoThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new EVOThread(r);
    }
}
