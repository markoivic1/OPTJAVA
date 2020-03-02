package hr.fer.zemris.optjava.rng.rngimpl;

import hr.fer.zemris.optjava.rng.IRNG;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class RNGRandomImpl implements IRNG {

    private Random random;
    public RNGRandomImpl() {
        random = new Random();
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public double nextDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    @Override
    public float nextFloat() {
        return random.nextFloat();
    }

    @Override
    public float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public double nextGaussian() {
        return random.nextGaussian();
    }
}
