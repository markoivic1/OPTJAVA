package hr.fer.zemris.optjava.dz8.evaluators;

import hr.fer.zemris.optjava.dz8.Elman;
import hr.fer.zemris.optjava.dz8.IReadOnlyDataset;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ElmanEvaluator implements Evaluator {
    private Elman elman;
    private IReadOnlyDataset dataset;

    public ElmanEvaluator(Elman elman, IReadOnlyDataset dataset) {
        this.elman = elman;
        this.dataset = dataset;
    }

    public double getAffinity(double[] weights, IReadOnlyDataset dataset) {
        double affinity = 0;
        for (int i = 0; i < dataset.getSize() - 1; i++) {
            double output = elman.calcOutput(i, weights, dataset);

            affinity += (dataset.getSample(i + 1) - output)
                    * (dataset.getSample(i + 1) - output);
        }
        return affinity / (dataset.getSize() - 1);
    }

    @Override
    public int getSize() {
        return elman.getParametersCount();
    }

    @Override
    public double[] generateUnit(int dimension, double lower, double upper, Random random) {
        double[] unit = new double[dimension];
        for (int i = 0; i < elman.getWeights(); i++) {
            unit[i] = lower + random.nextDouble() * (upper - lower);
        }
        return unit;
    }

    public Elman getElman() {
        return elman;
    }
}
