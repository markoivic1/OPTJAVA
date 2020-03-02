package hr.fer.zemris.optjava.dz8;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface IReadOnlyDataset {
    int getSize();

    double getSample(int index);
}
