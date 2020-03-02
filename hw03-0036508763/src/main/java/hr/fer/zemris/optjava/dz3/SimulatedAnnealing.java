package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.dz3.decoders.IDecoder;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solutions.DoubleArraySolution;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SimulatedAnnealing<T> implements IOptAlgorithm<T> {
    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;
    private Random random;
    private ITempSchedule tempSchedule;
    public SimulatedAnnealing(IDecoder<T> decoder, INeighborhood<T> neighborhood,
                              T startWith, IFunction function, boolean minimize,
                              ITempSchedule schedule) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
        this.tempSchedule = schedule;
        this.random = new Random();
    }

    public void run() {
        double[] values = decoder.decode(startWith);
        DoubleArraySolution oldSolution = new DoubleArraySolution(values.length);
        oldSolution.setValues(values);
        oldSolution.setFitness(function.valueAt(values));
        T oldValue = startWith;
        int k = 0;

        while (tempSchedule.getOuterLoopCounter() > 0) {
            double currentTemp = tempSchedule.getNextTemperature();
            while (tempSchedule.getInnerLoopCounter() > 0) {
                T currentValue = neighborhood.randomNeighbor(oldValue);
                DoubleArraySolution currentSolution = new DoubleArraySolution(values.length);
                double[] currentValues = decoder.decode(currentValue);
                currentSolution.setValues(currentValues);
                currentSolution.setFitness(function.valueAt(currentValues));
                double diff = minimize ? (currentSolution.getFitness() - oldSolution.getFitness()) : oldSolution.getFitness() - currentSolution.getFitness();
                if (diff <= 0) {

                    oldSolution = currentSolution;
                    oldValue = currentValue;
                } else {
                    if (random.nextDouble() < Math.exp(-diff / currentTemp)) {
                        oldSolution = currentSolution;
                        oldValue = currentValue;
                    }
                }
                System.out.print("Deviation: ");
                System.out.print(function.valueAt(oldSolution.getValues()));
                System.out.print(" Values: ");
                Arrays.stream(oldSolution.getValues()).forEach(v -> System.out.print(v + " "));
                System.out.print("temp: " + currentTemp);
                System.out.println();
            }
        }
    }
}
