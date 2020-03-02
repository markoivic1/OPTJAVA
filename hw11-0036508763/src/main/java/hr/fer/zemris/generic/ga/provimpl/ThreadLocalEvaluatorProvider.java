package hr.fer.zemris.generic.ga.provimpl;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.EvaluatorProvider;
import hr.fer.zemris.generic.ga.IGAEvaluator;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ThreadLocalEvaluatorProvider implements EvaluatorProvider {
    private ThreadLocal<Evaluator> threadLocal = new ThreadLocal<>();

    @Override
    public Evaluator getEvaluator(GrayScaleImage template) {
        if (threadLocal.get() == null) {
            Evaluator evaluator = new Evaluator(template);
            threadLocal.set(evaluator);
        }
        return threadLocal.get();
    }
}
