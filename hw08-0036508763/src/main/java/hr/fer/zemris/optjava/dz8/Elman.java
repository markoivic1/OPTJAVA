package hr.fer.zemris.optjava.dz8;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Elman {
    private int[] layers;
    private ITransferFunction[] functions;
    private int[][] neurons;

    public Elman(int[] layers, ITransferFunction[] functions) {
        if (layers[0] != 1 || layers[layers.length - 1] != 1) {
            throw new InvalidSizeException();
        }
        this.layers = layers;
        this.functions = functions;
        initNeurons();
    }

    private void initNeurons() {
        int neuronCount = 0;
        for (int i = 1; i < layers.length; i++) {
            if (i == 1) {
                neuronCount += 2 * layers[i];
            } else {
                neuronCount += layers[i];
            } 
        }
        neurons = new int[neuronCount][0]; // neurons + values for context neurons
        int neuronIndex = 0;
        int index = 0;
        for (int i = 1; i < layers.length; i++) {
            if (i == 1) {
                for (int j = 0; j < layers[i]; j++) {
                    neurons[neuronIndex] = new int[layers[1]];
                    for (int k = 0; k < neurons[j].length; k++) {
                        neurons[j][k] = index;
                        index++;
                    }
                    neuronIndex++;
                }
            }
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

    public int getParametersCount() {
        int parameterCount = getWeights();
        parameterCount += layers[1]; // add context values
        return parameterCount;
    }

    public int getWeights() {
        int weightCount = 0;
        for (int i = 1; i < layers.length; i++) {
            weightCount += layers[i] * (layers[i - 1] + 1);
        }
        weightCount += layers[1] * (layers[1] + 1); // add context weights
        return weightCount;
    }

    public double calcOutput(int inputIndex, double[] parameters, IReadOnlyDataset dataset) {
        double[] currentInputs = {dataset.getSample(inputIndex)};
        int currentNeuron = layers[1];
        // {context neuron, neuron, context values}
        for (int i = 1; i < layers.length; i++) {
            double[] nextInputs = new double[layers[i]];

            for (int j = 0; j < layers[i]; j++) {
                // bias is on the first place
                nextInputs[j] += parameters[neurons[currentNeuron][0]];

                // neuron parameters
                for (int k = 0; k < currentInputs.length; k++) {
                    nextInputs[j] += currentInputs[k] * parameters[neurons[currentNeuron][k + 1]];
                }
                // context neuron parameters
                if (i == 1) {
                    for (int k = 0; k < layers[1]; k++) {
                        nextInputs[j] += parameters[parameters.length - layers[1]] * parameters[neurons[j][k]];
                    }
                }
                nextInputs[j] = functions[i - 1].apply(nextInputs[j]);
                currentNeuron++;
            }
            if (i == 1) {
                saveContext(nextInputs, parameters);
            }
            currentInputs = nextInputs;
        }
        return currentInputs[0];
    }

    private void saveContext(double[] context, double[] parameters) {
        for (int i = parameters.length - context.length; i < parameters.length; i++) {
            parameters[i] = context[i - parameters.length + context.length];
        }
    }

}
