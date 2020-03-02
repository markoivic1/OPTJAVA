package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.EP;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.GASolutionImpl;
import hr.fer.zemris.optjava.dz11.mutator.*;
import hr.fer.zemris.optjava.rng.EVOThread;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import static hr.fer.zemris.optjava.dz11.GAUtil.*;

/**
 * ./11-kuca-200-133.png 200 500 10000 1 ./house-data.txt ./house.png
 * when using best individuals from the population for the next generation (fitness > -450000)
 * both parents were chosen by kTournament
 * 2 * gauss(20) + 1 * switch mutators
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Pokretac1 {
    private static GASolution<int[]> POISON_PILL = new GASolutionImpl<>(null);
    private static double CHANCE = 0.01; // 0.025 0.05
    private static int K = 5;
    private final static Semaphore nonEvaluatedSemaphore = new Semaphore(0);
    private final static Semaphore evaluatedSemaphore = new Semaphore(0);

    public static void main(String[] args) throws IOException {
        if (args.length != 7) {
            System.out.println("Invalid number of arguments");
            return;
        }
        GrayScaleImage template = GrayScaleImage.load(new File(args[0]));
        int nRectangles = Integer.parseInt(args[1]);
        int popSize = Integer.parseInt(args[2]);
        int nGenerations = Integer.parseInt(args[3]);
        double minFitness = Double.parseDouble(args[4]);
        String optimalParametersPath = args[5];
        String imagePath = args[6];

        ConcurrentLinkedQueue<GASolution<int[]>> nonEvaluatedQueue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<GASolution<int[]>> evaluatedQueue = new ConcurrentLinkedQueue<>();
        initThreads(nonEvaluatedQueue, evaluatedQueue, template);
        List<GASolution<int[]>> population =
                initAndEvaluatePopulation(popSize, nRectangles, template, nonEvaluatedQueue, evaluatedQueue);
        List<Mutator> mutators = new ArrayList<>();
        mutators.add(new GaussMutator(30));
        mutators.add(new GaussMutator(20));
        mutators.add(new GaussMutator(20));
        mutators.add(new GaussMutator(15));
        mutators.add(new RectangleMutator());
        mutators.add(new SwitchMutator(1, 2));
        Mutator mutator = new CombinedMutator(mutators);
        long before = System.currentTimeMillis();
        run(population, nGenerations, minFitness, template, mutator, popSize, nonEvaluatedQueue, evaluatedQueue);
        long after = System.currentTimeMillis();
        System.out.println("Time passed in seconds: " + ((double) (after - before) / (1000)));
        GASolution<int[]> best = getBest(population);
        GrayScaleImage bestImage = new GrayScaleImage(template.getWidth(), template.getHeight());
        System.out.println("fitness: " + best.fitness);
        EP.getEP(template).draw(best, bestImage);
        bestImage.save(new File(imagePath));
        Util.saveData(best, optimalParametersPath);
        poisonThreads(nonEvaluatedQueue);
    }


    private static void run(List<GASolution<int[]>> population, int nGenerations, double minFitness, GrayScaleImage template
            , Mutator mutator, int popSize
            , ConcurrentLinkedQueue<GASolution<int[]>> nonEvaluatedQueue
            , ConcurrentLinkedQueue<GASolution<int[]>> evaluatedQueue) {
        List<GASolution<int[]>> currentPopulation = new ArrayList<>(population);
        Collections.sort(currentPopulation);
        for (int generation = 0; generation < nGenerations; generation++) {
            for (int i = 0; i < currentPopulation.size(); i++) {
                GASolution<int[]> parent1 = kTournament(currentPopulation, K);
                GASolution<int[]> parent2 = kTournament(currentPopulation, K);
                while (parent1 == parent2) {
                    parent2 = kTournament(currentPopulation, K);
                }
                GASolution<int[]> child = crossover(parent1, parent2);
                mutator.mutate(child, template, CHANCE);
                nonEvaluatedQueue.offer(child);
                nonEvaluatedSemaphore.release();
            }
            for (int i = 0; i < popSize; i++) {
                GASolution<int[]> solution;
                while (true) {
                    try {
                        evaluatedSemaphore.acquire();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
                solution = evaluatedQueue.poll();
                insert(currentPopulation, solution);
            }
            currentPopulation.subList(popSize, currentPopulation.size()).clear();
            double bestFitness = currentPopulation.get(0).fitness;
            if (bestFitness > minFitness) {
                break;
            }
        }
        population.clear();
        population.addAll(currentPopulation);
    }

    private static void poisonThreads(ConcurrentLinkedQueue<GASolution<int[]>> nonEvaluatedQueue) {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            nonEvaluatedQueue.offer(POISON_PILL);
            nonEvaluatedSemaphore.release();
        }
    }

    private static void initThreads(ConcurrentLinkedQueue<GASolution<int[]>> nonEvaluatedQueue, ConcurrentLinkedQueue<GASolution<int[]>> evaluatedQueue, GrayScaleImage template) {
        Runnable job = () -> {
            GASolution<int[]> solution;
            while (true) {
                while (true) {
                    try {
                        nonEvaluatedSemaphore.acquire();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
                solution = nonEvaluatedQueue.poll();
                if (solution == POISON_PILL) {
                    return;
                }
                EP.getEP(template).evaluate(solution);
                evaluatedQueue.offer(solution);
                evaluatedSemaphore.release();
            }
        };
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread thread = new EVOThread(job);
            thread.start();
        }
    }

    private static List<GASolution<int[]>> initAndEvaluatePopulation(int popSize, int nRectangles, GrayScaleImage template
            , ConcurrentLinkedQueue<GASolution<int[]>> nonEvaluatedQueue
            , ConcurrentLinkedQueue<GASolution<int[]>> evaluatedQueue) {
        List<GASolution<int[]>> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            nonEvaluatedQueue.offer(createUnit(nRectangles, template));
            nonEvaluatedSemaphore.release();
        }
        for (int i = 0; i < popSize; i++) {
            GASolution<int[]> evaluatedSolution;
            while (true) {
                try {
                    evaluatedSemaphore.acquire();
                    break;
                } catch (InterruptedException e) {
                }
            }
            evaluatedSolution = evaluatedQueue.poll();

            population.add(evaluatedSolution);
        }
        return population;
    }
}
