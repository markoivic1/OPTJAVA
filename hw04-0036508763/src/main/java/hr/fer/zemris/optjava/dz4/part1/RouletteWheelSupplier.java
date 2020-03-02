package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.IFunction;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class RouletteWheelSupplier implements Supplier<Double[]> {
    private IFunction function;
    private List<Double[]> population;
    private double orderOfMagnitude;
    private Random random;

    // works for 30 0.0001 50000 rouletteWheel 0.03
    public RouletteWheelSupplier(IFunction function, List<Double[]> population, double orderOfMagnitude, Random random) {
        this.function = function;
        this.population = population;
        this.orderOfMagnitude = orderOfMagnitude;
        this.random = random;
    }

    /**
     * expects that population is sorted from lowest deviation
     *
     * @return
     */
    private Double[] selectParent() {
        double[] chances = new double[population.size()];
        double lowestChance = 0;
        // fill chances array with values of function
        for (int i = 0; i < population.size(); i++) {
            if (i == 0) {
                lowestChance = -function.valueAt(population.get(i));
                chances[i] = lowestChance;
                continue;
            }
            double currentChance = -function.valueAt(population.get(i));

            /*if (Double.isInfinite(currentChance) || currentChance / -function.valueAt(population.get(0)) > orderOfMagnitude) {
                chances[i] = 1;
                continue;
            }*/

            if (currentChance < lowestChance) {
                lowestChance = currentChance;
            }
            chances[i] = currentChance;
        }
        // sum of function values later used to norm chances
        double sum = 0;
        boolean once = true;
        for (int i = 0; i < chances.length; i++) {
            if (function.valueAt(population.get(population.size() - 1)) / function.valueAt(population.get(0)) > orderOfMagnitude) {
                chances[i] = -Math.log10(-chances[i]);
                if (once) {
                    lowestChance = -Math.log(-lowestChance);
                    once = false;
                }
            }
            if (chances[i] == 1) {
                chances[i] = 0;
                continue;
            }
            chances[i] -= lowestChance;
            sum += chances[i];
        }
        // norm chances
        for (int i = 0; i < chances.length; i++) {
            chances[i] /= sum;
            if (i > 0) {
                chances[i] += chances[i - 1];
            }
        }
        double randomNumber = random.nextDouble();
        int selectedParent = -1;
        for (int i = 0; i < chances.length; i++) {
            if (chances[i] >= randomNumber) {
                selectedParent = i;
                break;
            }
        }
        return population.get(selectedParent);
    }

    @Override
    public Double[] get() {
        return selectParent();
    }
}
