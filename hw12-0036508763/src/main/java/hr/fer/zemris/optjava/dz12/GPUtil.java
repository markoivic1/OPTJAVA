package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.function.IfFoodAhead;
import hr.fer.zemris.optjava.dz12.function.Prog2;
import hr.fer.zemris.optjava.dz12.function.Prog3;
import hr.fer.zemris.optjava.dz12.terminal.TMove;
import hr.fer.zemris.optjava.dz12.terminal.TRotateLeft;
import hr.fer.zemris.optjava.dz12.terminal.TRotateRight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class GPUtil {
    private static Random random = new Random();
    private static int[][] mapCopy;

    private static Node generateFullTree(int depthsLeft) {
        if (depthsLeft == 1) {
            return getRandomTerminal();
        }
        int function = random.nextInt(3);
        if (function == 1) {
            Node first = generateFullTree(depthsLeft - 1);
            Node second = generateFullTree(depthsLeft - 1);
            return new IfFoodAhead(first, second);
        } else if (function == 2) {

            Node first = generateFullTree(depthsLeft - 1);
            Node second = generateFullTree(depthsLeft - 1);
            return new Prog2(first, second);
        } else {
            Node first = generateFullTree(depthsLeft - 1);
            Node second = generateFullTree(depthsLeft - 1);
            Node third = generateFullTree(depthsLeft - 1);
            return new Prog3(first, second, third);
        }
    }

    private static Node generateGrowTree(int depthsLeft) {
        if (depthsLeft == 1) {
            return getRandomTerminal();
        }
        int nodeIndex = random.nextInt(6);
        if (nodeIndex == 1) {
            return new TMove();
        } else if (nodeIndex == 2) {
            return new TRotateLeft();
        } else if (nodeIndex == 3) {
            return new TRotateRight();
        } else if (nodeIndex == 4) {
            Node first = generateGrowTree(depthsLeft - 1);
            Node second = generateGrowTree(depthsLeft - 1);
            return new IfFoodAhead(first, second);
        } else if (nodeIndex == 5) {
            Node first = generateGrowTree(depthsLeft - 1);
            Node second = generateGrowTree(depthsLeft - 1);
            return new Prog2(first, second);
        } else {
            Node first = generateGrowTree(depthsLeft - 1);
            Node second = generateGrowTree(depthsLeft - 1);
            Node third = generateGrowTree(depthsLeft - 1);
            return new Prog3(first, second, third);
        }
    }

    private static Node getRandomTerminal() {
        int terminal = random.nextInt(3);
        if (terminal == 1) {
            return new TMove();
        } else if (terminal == 2) {
            return new TRotateLeft();
        } else {
            return new TRotateRight();
        }
    }

    public static Node generateGrow(int maxDepth) {
        if (maxDepth <= 0) {
            throw new IllegalArgumentException("Tree depth must be greater equal or greater than 1.");
        }
        Node node;
        int functionIndex = random.nextInt(3);
        if (functionIndex == 1) {
            node = new IfFoodAhead(generateGrowTree(maxDepth - 1), generateGrowTree(maxDepth - 1));
        } else if (functionIndex == 2) {
            node = new Prog2(generateGrowTree(maxDepth - 1), generateGrowTree(maxDepth - 1));
        } else {
            node = new Prog3(generateGrowTree(maxDepth - 1), generateGrowTree(maxDepth - 1), generateGrowTree(maxDepth - 1));
        }
        node.calculateNodeNumber(1);
        node.calculateNodeDepth(1);
        return node;
    }

    public static Node generateFull(int maxDepth) {
        if (maxDepth <= 0) {
            throw new IllegalArgumentException("Tree depth must be greater equal or greater than 1.");
        }
        Node node = generateFullTree(maxDepth);
        node.calculateNodeNumber(1);
        node.calculateNodeDepth(1);
        return node;
    }

    public static Node crossover(Node firstParent, Node secondParent, int maxDepth, int maxNodes) {
        Node firstParentCopy = firstParent.copy();
        int firstNodeNumber = random.nextInt(firstParentCopy.getDescendentSize()) + 2;
        int secondNodeNumber = random.nextInt(secondParent.getDescendentSize() + 1) + 1;
        Node secondParentsPart = secondParent.getNodeByNumber(secondNodeNumber);
        firstParentCopy.replace(secondParentsPart, firstNodeNumber);
        firstParentCopy.calculateNodeDepth(1);
        firstParentCopy.calculateNodeNumber(1);
        if ((firstParentCopy.getDescendentSize() + 1 > maxNodes) || (firstParentCopy.getTreeDepth() > maxDepth)) {
            return null;
        }
        return firstParentCopy;
    }

    public static Node mutate(Node child, int maxDepth, int maxNodes) {
        Node childCopy = child.copy();
        int nodeNumber = random.nextInt(childCopy.getDescendentSize() + 1) + 1;
        if (nodeNumber == 1) {
            if (random.nextBoolean()) {
                do {
                    childCopy = generateFull(random.nextInt(childCopy.getTreeDepth() - 1) + 2);
                } while (childCopy.getDescendentSize() + 1 > maxNodes);
            } else {
                do {
                    childCopy = generateGrow(random.nextInt(childCopy.getTreeDepth() - 1) + 2);
                } while (childCopy.getDescendentSize() + 1 > maxNodes);
            }
            childCopy.calculateNodeDepth(1);
            childCopy.calculateNodeNumber(1);
            return childCopy;
        }
        int newTreeDepth = childCopy.getNodeByNumber(nodeNumber).getMaxDepth(maxDepth);
        if (newTreeDepth == 0) {
            childCopy.replace(getRandomTerminal(), nodeNumber);
        } else {
            Node replacement;
            double chance = random.nextDouble();
            if (chance < 0.15) {
                replacement = generateFull(random.nextInt(newTreeDepth) + 2);
            } else if (chance > 0.5) {
                replacement = generateGrow(random.nextInt(newTreeDepth) + 2);
            } else {
                replacement = getRandomTerminal();
            }
            childCopy.replace(replacement, nodeNumber);
        }
        childCopy.calculateNodeDepth(1);
        childCopy.calculateNodeNumber(1);
        if ((childCopy.getDescendentSize() + 1 > maxNodes) || (childCopy.getTreeDepth() > maxDepth)) {
            return null;
        }
        return childCopy;
    }

    public static List<Node> initPopulation(int maxDepth, int maxNodes, int popSize) {
        List<Node> population = new ArrayList<>();
        for (int i = 2; i < maxDepth + 1; i++) {
            for (int j = 0; j < popSize / (maxDepth - 2 + 1) / 2; j++) {
                Node full = generateFull(i);
                while (full.getDescendentSize() + 1 > maxNodes) {
                    full = generateFull(i);
                }
                full.calculateNodeDepth(1);
                population.add(full);
                Node grow = generateGrow(i);
                while (grow.getDescendentSize() + 1 > maxNodes) {
                    grow = generateGrow(i);
                }
                population.add(grow);
            }
        }
        return population;
    }

    public static int kTournament(double[] fitness, int k) {
        List<Integer> indexes = new ArrayList<>();
        do {
            int index = random.nextInt(fitness.length);
            if (!indexes.contains(index)) {
                indexes.add(index);
            }
        } while (indexes.size() != k);
        int bestIndex = indexes.get(0);
        for (int i = 1; i < indexes.size(); i++) {
            if (fitness[indexes.get(i)] > fitness[bestIndex]) {
                bestIndex = indexes.get(i);
            }
        }
        return bestIndex;
    }

    public static void evaluate(List<Node> population, Ant ant, int[][] map, int[] foodEaten, double[] fitness) {
        if (mapCopy == null) {
            mapCopy = new int[map.length][map[0].length];
        }
        for (int i = 0; i < population.size(); i++) {
            resetMap(map, mapCopy);
            ant.reset();
            while (!ant.noActionsLeft()) {
                population.get(i).execute(ant, mapCopy);
            }
            foodEaten[i] = ant.getFoodEaten();
            fitness[i] = ant.getFoodEaten();
        }
        resetMap(map, mapCopy);
        ant.reset();
    }

    public static void evaluate(Node node, Ant ant, int[][] map, int[] foodEaten, double[] fitness, int index) {
        if (mapCopy == null) {
            mapCopy = new int[map.length][map[0].length];
        }
        resetMap(map, mapCopy);
        ant.reset();
        while (!ant.noActionsLeft()) {
            node.execute(ant, mapCopy);
        }
        foodEaten[index] = ant.getFoodEaten();
        fitness[index] = ant.getFoodEaten();
        resetMap(map, mapCopy);
        ant.reset();
    }

    public static void resetMap(int[][] original, int[][] modified) {
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                modified[i][j] = original[i][j];
            }
        }
    }

    public static int getBest(List<Node> population, double[] fitness) {
        int best = 0;
        for (int i = 1; i < population.size(); i++) {
            if (fitness[i] > fitness[best]) {
                best = i;
            }
        }
        return best;
    }

}
