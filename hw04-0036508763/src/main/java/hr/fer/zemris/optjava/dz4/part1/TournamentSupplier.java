package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.IFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TournamentSupplier implements Supplier<Double[]> {
    private IFunction function;
    private List<Double[]> population;
    private Random random;
    private int tournamentSize;

    // works for 100 0.0001 50000 tournament:5 0.03
    public TournamentSupplier(IFunction function, List<Double[]> population, Random random, int tournamentSize) {
        this.function = function;
        this.population = population;
        this.random = random;
        this.tournamentSize = tournamentSize;
    }

    private Double[] selectParent() {
        List<Integer> selectedIndexes = new ArrayList<>();
        Double[] bestParent = new Double[0];
        while (selectedIndexes.size() != tournamentSize) {
            int randomIndex = Math.abs(random.nextInt()) % population.size();
            if (selectedIndexes.contains(randomIndex)) {
                continue;
            }
            selectedIndexes.add(randomIndex);
            if (selectedIndexes.size() == 1) {
                bestParent = population.get(randomIndex);
                continue;
            }
            if (function.valueAt(bestParent) > function.valueAt(population.get(randomIndex))) {
                bestParent = population.get(randomIndex);
            }
        }
        return bestParent;
    }
    @Override
    public Double[] get() {
        return selectParent();
    }
}
