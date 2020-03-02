package hr.fer.zemris.generic.ga.provimpl;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.EvaluatorProvider;
import hr.fer.zemris.generic.ga.IGAEvaluator;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ThreadBoundEvaluatorProvider implements EvaluatorProvider {
    @Override
    public Evaluator getEvaluator(GrayScaleImage template) {
        return ((EvaluatorProvider) Thread.currentThread()).getEvaluator(template);
    }
}
