package hr.fer.zemris.optjava.dz8;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TanHyperTransferFunction implements ITransferFunction {
    @Override
    public double apply(double value) {
        double exp = Math.exp(-value);
        return (1 - exp) / (1 + exp);
    }
}
