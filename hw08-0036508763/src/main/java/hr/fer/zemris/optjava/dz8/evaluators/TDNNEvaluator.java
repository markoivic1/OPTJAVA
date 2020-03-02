package hr.fer.zemris.optjava.dz8.evaluators;

import hr.fer.zemris.optjava.dz8.IReadOnlyDataset;
import hr.fer.zemris.optjava.dz8.TDNN;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TDNNEvaluator implements Evaluator {

    private TDNN tdnn;
    private IReadOnlyDataset dataset;
    private int l;

    public TDNNEvaluator(TDNN tdnn, IReadOnlyDataset dataset, int l) {
        this.tdnn = tdnn;
        this.dataset = dataset;
        this.l = l;
    }

    @Override
    public double getAffinity(double[] weights, IReadOnlyDataset dataset) {
        double affinity = 0;
        for (int i = 0; i < dataset.getSize() - l; i++) {
            double[] inputs = new double[l];
            for (int j = i; j < i + l; j++) {
                inputs[j - i] = dataset.getSample(j);
            }

            double output = tdnn.calcOutput(inputs, weights);

            affinity += (dataset.getSample(i + l) - output)
                    * (dataset.getSample(i + l) - output);
        }
        return affinity / (dataset.getSize() - l);
    }

    @Override
    public int getSize() {
        return tdnn.getWeightsCount();
    }

    @Override
    public double[] generateUnit(int dimension, double lower, double upper, Random random) {
        double[] unit = new double[dimension];
        for (int i = 0; i < unit.length; i++) {
            unit[i] = lower + random.nextDouble() * (upper - lower);
        }
        return unit;
    }
}
