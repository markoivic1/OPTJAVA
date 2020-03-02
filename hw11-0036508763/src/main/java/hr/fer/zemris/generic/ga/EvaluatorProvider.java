package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface EvaluatorProvider {
    Evaluator getEvaluator(GrayScaleImage template);
}
