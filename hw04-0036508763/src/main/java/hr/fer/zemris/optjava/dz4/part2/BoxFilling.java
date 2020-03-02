package hr.fer.zemris.optjava.dz4.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class BoxFilling {

    private static int POP_SIZE = 15;
    private static int n;
    private static int m;
    private static boolean p;
    private static int maxIter;
    private static int acceptableContainerSize;
    private static List<Box> population;
    private static Random random = new Random();
    private static int height;
    private static int MAX_SIZE;

    // ./kutije/problem-20-50-1.dat 15 5 true 30000 15
    // ./kutije/problem-20-50-1.dat 5 3 true 60000 15
    // ./kutije/problem-20-50-3.dat 5 1 true 60000 15
    // With adjusted height percentage in Box class chance of finding best possible solution is 100%
    public static void main(String[] args) throws IOException {
        if (args.length != 6) {
            System.out.println("Invalid number of arguments.");
            return;
        }
        height = Integer.parseInt(args[0].substring("./kutije/problem-".length(), "./kutije/problem-xx".length()));
        population = Files.readAllLines(Paths.get(args[0])).stream().map(s -> s.substring(1, s.length() - 1))
                .map(s -> s.split(", ")).map(s -> new Box(height, s)).collect(Collectors.toList());
        for (int i = 0; i < POP_SIZE - 1; i++) {
            population.add(population.get(0).clone());
            Util.scramble(population.get(i + 1));
        }
        n = Integer.parseInt(args[1]);
        m = Integer.parseInt(args[2]);
        p = Boolean.parseBoolean(args[3]);
        maxIter = Integer.parseInt(args[4]);
        acceptableContainerSize = Integer.parseInt(args[5]);
        long millis = System.currentTimeMillis();
        runSelection();
        System.out.println("Time: " + (System.currentTimeMillis() - millis) + "ms");
    }

    private static void runSelection() {
        MAX_SIZE = population.get(0).getSticks().size();
        double bestFitness = 0;
        for (int i = 0; i < maxIter; i++) {
            Box parent1 = selectParent(n,true);
            Box parent2 = selectParent(n,true);
            Box child = parent1.cross(parent2);
            Box contestor = selectParent(m,false);

            if (p && (child.getFitness(MAX_SIZE) > contestor.getFitness(MAX_SIZE))) {
                contestor.setSticks(child.getSticks());
            }
            if (!p) {
                contestor.setSticks(child.getSticks());
            }
            double currentFitness = getBest().getFitness(MAX_SIZE);
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
                System.out.println(getBest().getFitness(MAX_SIZE));
            }
        }
        System.out.println();
        System.out.println("This arrengment has taken: " + getBest().getSticks().size() + " columns.");
        Util.printContainer(getBest().getSticks());
    }

    private static Box getBest() {
        Box best = population.get(0);
        for (Box unit : population) {
            if (unit.getFitness(MAX_SIZE) > best.getFitness(MAX_SIZE)) {
                best = unit;
            }
        }
        return best;
    }

    private static Box selectParent(int size, boolean best) {
        Box bestParent;
        Set<Integer> pickedNumbers = new HashSet<>();
        while (pickedNumbers.size() < size) {
            pickedNumbers.add(Math.abs(random.nextInt()) % population.size());
            if (pickedNumbers.size() == population.size()) {
                return getBest();
            }
        }
        bestParent = population.get(Math.abs(random.nextInt()) % population.size());
        for (Integer number : pickedNumbers) {
            if ((bestParent.getFitness(MAX_SIZE) > population.get(number).getFitness(MAX_SIZE)) ^ !best) {
                bestParent = population.get(number);
            }
        }

        return bestParent;
    }
}
