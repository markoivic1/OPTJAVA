package hr.fer.zemris.optjava.dz8;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TDNN {
    private int[] layers;
    private ITransferFunction[] functions;
    private int[][] neurons;

    public TDNN(int[] layers, ITransferFunction[] functions) {
        this.layers = layers;
        this.functions = functions;
        initNeurons();
    }

    private void initNeurons() {
        int neuronCount = 0;
        for (int i = 1; i < layers.length; i++) {
            neuronCount += layers[i];
        }
        neurons = new int[neuronCount][0];
        int neuronIndex = 0;
        int index = 0;
        for (int i = 1; i < layers.length; i++) {
            for (int j = 0; j < layers[i]; j++) {
                neurons[neuronIndex] = new int[layers[i - 1] + 1];
                for (int k = 0; k < neurons[neuronIndex].length; k++) {
                    neurons[neuronIndex][k] = index;
                    index++;
                }
                neuronIndex++;
            }
        }
    }

    public int getWeightsCount() {
        int weightCount = 0;
        for (int i = 1; i < layers.length; i++) {
            weightCount += layers[i] * (layers[i - 1] + 1);
        }
        return weightCount;
    }

    public double calcOutput(double[] inputs, double[] weights) {
        double[] currentInputs = inputs;
        int currentNeuron = 0;
        for (int i = 1; i < layers.length; i++) {
            double[] nextInputs = new double[layers[i]];

            for (int j = 0; j < layers[i]; j++) {
                // bias is on the first place
                nextInputs[j] += weights[neurons[currentNeuron][0]];

                // weights
                for (int k = 0; k < currentInputs.length; k++) {
                    nextInputs[j] += currentInputs[k] * weights[neurons[currentNeuron][k + 1]];
                }
                nextInputs[j] = functions[i - 1].apply(nextInputs[j]);
                currentNeuron++;
            }
            currentInputs = nextInputs;
        }
        return currentInputs[0];
    }
}
