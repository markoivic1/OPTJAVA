package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.evaluators.ElmanEvaluator;
import hr.fer.zemris.optjava.dz8.evaluators.Evaluator;
import hr.fer.zemris.optjava.dz8.evaluators.TDNNEvaluator;
import hr.fer.zemris.optjava.dz8.operators.UniformCrossover;

import java.util.Arrays;

/**
 * 08-Laser-generated-data.txt elman-1x10x1 60 0.01 10000
 * 08-Laser-generated-data.txt tdnn-8x10x1 60 0.01 10000
 *
 * When using exponential crossover both NNs prefer larger chance of taking mutated vector's part (CHANCE ~ 0.5)
 * uniform crossover works best for lower chances (CHANCE = 0.1)
 *
 * Preferred factor F seems to be 0.5 for both crossovers
 *
 * tdnn generally works fine when using uniform crossover although a bit slower than exponential crossover
 * elman nn works a bit slower than tdnn when using exponential crossover and works much slower when using uniform
 * crossover
 *
 * best trial vector strategy is DE/best/1/bin
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ANNTrainer {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Invalid number of arguments");
            return;
        }
        IReadOnlyDataset dataset = Util.loadData(args[0], 600);

        int[] layers = initLayers(args[1].split("-")[1].split("x"));
        int popSize = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);
        ITransferFunction[] functions = new ITransferFunction[layers.length];
        Arrays.fill(functions, new TanHyperTransferFunction());
        Evaluator evaluator;
        if (args[1].startsWith("tdnn")) {
            evaluator = new TDNNEvaluator(new TDNN(layers, functions), dataset, layers[0]);
        } else if (args[1].startsWith("elman")) {
            evaluator = new ElmanEvaluator(new Elman(layers, functions), dataset);
        } else {
            System.out.println("Unsupported evaluator");
            return;
        }
        Algorithm algorithm = new DEAlgorithm(dataset, new UniformCrossover(), popSize, merr, maxIter);
        double[] bestSolution = algorithm.run(evaluator);
        printSolution(dataset, evaluator, bestSolution);
    }

    private static void printSolution(IReadOnlyDataset dataset, Evaluator evaluator, double[] bestSolution) {
        System.out.println("Best solution has an error of: " + evaluator.getAffinity(bestSolution, dataset));
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < bestSolution.length; i++) {
            sb.append(bestSolution[i]).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb);
    }

    private static int[] initLayers(String[] stringLayers) {
        int[] layers = new int[stringLayers.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = Integer.parseInt(stringLayers[i]);
        }
        return layers;
    }
}
