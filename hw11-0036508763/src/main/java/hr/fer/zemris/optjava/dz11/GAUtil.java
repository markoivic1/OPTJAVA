package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.GASolutionImpl;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GAUtil {
    public static void insert(List<GASolution<int[]>> population, GASolution<int[]> child) {
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).fitness < child.fitness) {
                population.add(i, child);
                return;
            }
        }
        population.add(child);
    }

    public static GASolution<int[]> kTournament(List<GASolution<int[]>> population, int k) {
        IRNG rng = RNG.getRNG();
        List<Integer> indexes = new ArrayList<>();
        while (indexes.size() != k) {
            Integer index = rng.nextInt(0, population.size());
            if (!indexes.contains(index)) {
                indexes.add(index);
            }
        }
        return getBest(population, indexes);
    }

    public static GASolution<int[]> getBest(List<GASolution<int[]>> population, List<Integer> indexes) {
        GASolution<int[]> best = population.get(indexes.get(0));
        for (int i = 1; i < indexes.size(); i++) {
            if (population.get(indexes.get(i)).fitness > best.fitness) {
                best = population.get(indexes.get(i));
            }
        }
        return best;
    }

    public static GASolution<int[]> getWorst(List<GASolution<int[]>> population) {
        GASolution<int[]> worst = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).fitness < worst.fitness) {
                worst = population.get(i);
            }
        }
        return worst;
    }


    public static GASolution<int[]> getBest(List<GASolution<int[]>> population) {
        GASolution<int[]> best = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).fitness > best.fitness) {
                best = population.get(i);
            }
        }
        return best;
    }

    public static GASolution<int[]> crossover(GASolution<int[]> parent1, GASolution<int[]> parent2) {
        IRNG rng = RNG.getRNG();
        int[] child = new int[parent1.getData().length];
        child[0] = rng.nextBoolean() ? parent1.getData()[0] : parent2.getData()[0];
        boolean pickedFirstParent;
        for (int i = 1; i < parent1.getData().length; i += 5) {
            pickedFirstParent = rng.nextBoolean();
            for (int j = 0; j < 5; j++) {
                if (pickedFirstParent) {
                    child[i + j] = parent1.getData()[i + j];
                } else {
                    child[i + j] = parent2.getData()[i + j];
                }
            }
        }
        return new GASolutionImpl<>(child);
    }


    public static GASolution<int[]> getRandomParent(List<GASolution<int[]>> population) {
        return population.get(RNG.getRNG().nextInt(0, population.size()));
    }


    public static GASolutionImpl<int[]> createUnit(int nRectangles, GrayScaleImage template) {
        IRNG rng = RNG.getRNG();
        int[] solution = new int[1 + 5 * nRectangles];
        solution[0] = rng.nextInt(0, 255);
        for (int j = 1; j < 5 * nRectangles; j += 5) {
            solution[j] = rng.nextInt(0, template.getWidth());
            solution[j + 1] = rng.nextInt(0, template.getHeight());
            solution[j + 2] = rng.nextInt(1, template.getWidth() - solution[j] + 1);
            solution[j + 3] = rng.nextInt(1, template.getHeight() - solution[j + 1] + 1);
            solution[j + 4] = rng.nextInt(0, 255);
        }
        return new GASolutionImpl<>(solution);
    }
}
