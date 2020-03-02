package hr.fer.zemris.optjava.dz7;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SigmoidTransferFunction implements ITransferFunction {
    @Override
    public double apply(double value) {
        return 1 / (1 + Math.exp(-value));
    }
}
