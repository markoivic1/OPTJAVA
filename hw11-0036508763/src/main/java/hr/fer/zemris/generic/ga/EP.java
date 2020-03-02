package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class EP {
    private static EvaluatorProvider evaluatorProvider;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("./src/main/resources/eval-config.properties"));
        } catch (IOException e) {
            System.out.println("Unable to load properties.");
        }
        try {
            evaluatorProvider = (EvaluatorProvider) ClassLoader.getSystemClassLoader()
                    .loadClass(properties.getProperty("eval-provider"))
                    .getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Evaluator getEP(GrayScaleImage template) {
        return evaluatorProvider.getEvaluator(template);
    }
}
