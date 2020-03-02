package hr.fer.zemris.optjava.dz3.neighborhood;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface INeighborhood<T> {
    T randomNeighbor(T solution);
}
