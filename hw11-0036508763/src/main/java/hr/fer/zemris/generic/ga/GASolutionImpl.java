package hr.fer.zemris.generic.ga;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GASolutionImpl<T> extends GASolution<T> {

    public GASolutionImpl(T data) {
        this.data = data;
    }

    @Override
    public GASolution<T> duplicate() {
        GASolutionImpl<T> copy = new GASolutionImpl<>(this.data);
        //copy.data = this.data;
        copy.fitness = this.fitness;
        return copy;
    }

}
