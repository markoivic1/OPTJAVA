package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;
import hr.fer.zemris.optjava.dz3.solutions.SingleObjectiveSolution;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GreedyAlgorithm<T> implements IOptAlgorithm<T> {

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;

    public GreedyAlgorithm(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith, IFunction function, boolean minimize) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
    }

    public void run() {
        double[] values = decoder.decode(startWith);
        DoubleArraySolution oldSolution = new DoubleArraySolution(values.length);
        oldSolution.setValues(values);
        oldSolution.setFitness(function.valueAt(values));
        T oldValue = startWith;

        for (int i = 0; i < 10000; i++) {
            T currentValue = neighborhood.randomNeighbor(oldValue);
            DoubleArraySolution currentSolution = new DoubleArraySolution(values.length);
            currentSolution.setValues(decoder.decode(currentValue));
            currentSolution.setFitness(function.valueAt(currentSolution.getValues()));

            int compare = oldSolution.compareTo(currentSolution);
            if (minimize) {
                if (compare > 0) {
                    oldValue = currentValue;
                    oldSolution = currentSolution;
                }
            } else {
                if (compare < 0) {
                    oldValue = currentValue;
                    oldSolution = currentSolution;
                }
            }
        }

        System.out.println(function.valueAt(oldSolution.getValues()));
    }
}
