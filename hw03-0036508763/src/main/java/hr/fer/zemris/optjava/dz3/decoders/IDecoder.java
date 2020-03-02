package hr.fer.zemris.optjava.dz3.decoders;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface IDecoder<T> {
    double[] decode(T solution);

    void decode(T solution, double[] array);
}
