package hr.fer.zemris.optjava.dz7;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface IReadOnlyDataset {
    int getSize();

    int getInputSize();

    int getOutputSize();

    double[] getSampleInput(int index);

    double[] getSampleOutput(int index);
}
