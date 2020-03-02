package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.EP;
import hr.fer.zemris.generic.ga.Evaluator;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.dz11.mutator.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static hr.fer.zemris.optjava.dz11.GAUtil.*;

/**
 * ./11-kuca-200-133.png 200 200 10000 350000 ./house-data.txt ./house.png
 *
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Pokretac2 {
    private static double CHANCE = 0.01; // 0.025 0.05
    private static int K = 5;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
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
        Evaluator evaluator = new Evaluator(template);
        List<GASolution<int[]>> population = initPopulation(popSize, nRectangles, template);
        List<Mutator> mutators = new ArrayList<>();
        mutators.add(new GaussMutator(30));
        mutators.add(new GaussMutator(20));
        mutators.add(new GaussMutator(20));
        mutators.add(new GaussMutator(15));
        mutators.add(new RectangleMutator());
        mutators.add(new SwitchMutator(1, 2));
        Mutator mutator = new CombinedMutator(mutators);

        long before = System.currentTimeMillis();
        run(population, nGenerations, minFitness, template, mutator, popSize);
        long after = System.currentTimeMillis();
        System.out.println("Time passed in seconds: " + ((double) (after - before) / (1000)));
        GASolution<int[]> best = getBest(population);
        GrayScaleImage bestImage = new GrayScaleImage(template.getWidth(), template.getHeight());
        System.out.println("fitness: " + best.fitness);
        evaluator.draw(best, bestImage);
        bestImage.save(new File(imagePath));
        Util.saveData(best, optimalParametersPath);
    }

    private static List<GASolution<int[]>> initPopulation(int popSize, int nRectangles, GrayScaleImage template) throws InterruptedException, ExecutionException {
        List<GASolution<int[]>> population = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new EvoThreadFactory());
        List<Callable<GASolution<int[]>>> jobs = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            jobs.add(new EvaluateJob(createUnit(nRectangles, template), template));
        }
        List<Future<GASolution<int[]>>> results = executorService.invokeAll(jobs);
        for (int i = 0; i < popSize; i++) {
            population.add(results.get(i).get());
        }
        executorService.shutdown();
        return population;
    }

    private static void run(List<GASolution<int[]>> population, int nGenerations, double minFitness
            , GrayScaleImage template, Mutator mutator, int popSize) throws InterruptedException, ExecutionException {

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, new EvoThreadFactory());
        Collections.sort(population);
        for (int generation = 0; generation < nGenerations; generation++) {
            List<Callable<List<GASolution<int[]>>>> jobs = new ArrayList<>();
            for (int j = 0; j < popSize / 5; j++) {
                jobs.add(new Job(population, 5, mutator, template));
            }
            List<Future<List<GASolution<int[]>>>> results = executorService.invokeAll(jobs);
            for (int i = 0; i < jobs.size(); i++) {
                List<GASolution<int[]>> children = results.get(i).get();
                for (int j = 0; j < children.size(); j++) {
                    GASolution<int[]> child = children.get(j);
                    insert(population, child);
                }
            }
            System.out.println(population.get(0).fitness);
            if (minFitness < population.get(0).fitness) {
                executorService.shutdown();
                return;
            }
            population.subList(popSize, population.size()).clear();
        }
        executorService.shutdown();
    }

    static class EvaluateJob implements Callable<GASolution<int[]>> {
        private GASolution<int[]> solution;
        private GrayScaleImage template;

        public EvaluateJob(GASolution<int[]> solution, GrayScaleImage template) {
            this.solution = solution;
            this.template = template;
        }

        @Override
        public GASolution<int[]> call() throws Exception {
            EP.getEP(template).evaluate(solution);
            return solution;
        }
    }


    static class Job implements Callable<List<GASolution<int[]>>> {

        private List<GASolution<int[]>> population;
        private int numberOfChildren;
        private Mutator mutator;
        private GrayScaleImage template;

        public Job(List<GASolution<int[]>> population, int numberOfChildren, Mutator mutator, GrayScaleImage template) {
            this.population = population;
            this.numberOfChildren = numberOfChildren;
            this.mutator = mutator;
            this.template = template;
        }

        @Override
        public List<GASolution<int[]>> call() throws Exception {
            List<GASolution<int[]>> children = new ArrayList<>();
            for (int i = 0; i < numberOfChildren; i++) {
                GASolution<int[]> parent1 = kTournament(population, K);
                GASolution<int[]> parent2 = kTournament(population, K);
                while (parent1 == parent2) {
                    parent2 = kTournament(population, K);
                }
                GASolution<int[]> child = crossover(parent1, parent2);
                mutator.mutate(child, template, CHANCE);
                EP.getEP(template).evaluate(child);
                children.add(child);
            }
            return children;
        }
    }
}
