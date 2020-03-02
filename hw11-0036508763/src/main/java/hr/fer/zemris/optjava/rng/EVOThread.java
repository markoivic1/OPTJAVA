package hr.fer.zemris.optjava.rng;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.EvaluatorProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class EVOThread extends Thread implements IRNGProvider, EvaluatorProvider {
    private IRNG rng = new RNGRandomImpl();
    private Evaluator evaluator;

    public EVOThread() {
    }

    public EVOThread(Runnable target) {
        super(target);
    }

    public EVOThread(String name) {
        super(name);
    }

    public EVOThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public EVOThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public EVOThread(Runnable target, String name) {
        super(target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name,
                     long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public IRNG getRNG() {
        return rng;
    }

    @Override
    public Evaluator getEvaluator(GrayScaleImage template) {
        if (evaluator == null) {
            evaluator = new Evaluator(template);
        }
        return evaluator;
    }
}