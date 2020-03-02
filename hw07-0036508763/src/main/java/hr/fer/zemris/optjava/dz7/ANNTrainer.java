package hr.fer.zemris.optjava.dz7;

import java.util.Arrays;

/**
 * Bias weights are located on the first index of each neuron and weights array.
 *
 * Smallest neural network which consistently gives good results for larger nn is {input, 3, output}.
 * When using {input, 3, output} and population size of 20 results can sometimes be bad.
 * Using slightly larger nn such as {input, 5, 3, output} alleviates this problem
 * while its not worsening results for larger population.
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class ANNTrainer {

    private static int n;
    private static double mError;
    private static int maxiter;
    private static double weightMax = 0.9;
    private static double weightMin = 0.4;
    private static double weight;
    private static double c1 = 2;
    private static double c2 = 2;
    private static double beta = 5;
    private static double ro = 0.25;

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Invalid number of arguments");
            return;
        }
        IReadOnlyDataset dataset = Util.loadData(args[0]);
        int[] layers = {dataset.getInputSize(), 5, 3, dataset.getOutputSize()};
        ITransferFunction[] functions = new ITransferFunction[layers.length];
        for (int i = 0; i < functions.length; i++) {
            functions[i] = new SigmoidTransferFunction();
        }
        FFANN ffann = new FFANN(layers, functions, dataset);
        n = Integer.parseInt(args[2]);
        mError = Double.parseDouble(args[3]);
        maxiter = Integer.parseInt(args[4]);

        if (args[1].equals("pso-a")) {
            ParticleSwarmOpt pso = new ParticleSwarmOpt(n, mError, maxiter, weightMax, weightMin, weight, c1, c2);
            double[] solution = pso.run(n / 2, ffann);
            Util.printComparison(ffann, dataset, solution);
        } else if (args[1].startsWith("pso-b")) {
            ParticleSwarmOpt pso = new ParticleSwarmOpt(n, mError, maxiter, weightMax, weightMin, weight, c1, c2);
            double[] solution = pso.run(Integer.parseInt(args[1].split("-")[2]), ffann);
            Util.printComparison(ffann, dataset, solution);
        } else if (args[1].equals("clonalg")) {
            int randomUnits = n / 10;
            ClonAlg clonAlg = new ClonAlg(n, mError, maxiter, randomUnits, beta, ro);
            double[] solution = clonAlg.run(ffann);
            Util.printComparison(ffann, dataset, solution);
        } else {
            System.out.println("Invalid algorithm name is given.");
        }
    }

    private static void testingExample(String arg) {
        IReadOnlyDataset dataset = Util.loadData(arg);
        FFANN ffann = new FFANN(
                new int[]{4, 5, 3, 3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction()
                },
                dataset);
        double[] weights = new double[ffann.getWeightsCount()];
        Arrays.fill(weights, 0.1);
        System.out.println(ffann.getAffinity(weights));
    }


}
