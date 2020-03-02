package hr.fer.zemris.optjava.dz6;

import javax.swing.text.ChangedCharSetException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 *
 *
 * ./datasets/bays29.tsp 1 30 10000
 */
public class TSPSolver {
    private static double[][] niWeightMatrix;
    private static double[][] distances;
    private static final double ALPHA = 1.73;
    private static final double BETA = 2;
    private static List<City> cities;
    private static int k;
    private static int ants;
    private static int maxIter;
    private static double[][] pheromone;
    private static final double RO = 0.02;
    private static double a;
    private static double tauMax;
    private static double tauMin;
    private static Random random = new Random();
    private static final int STAGNATION = 20;
    private static double PERCENTAGE = 0.4;

    //TODO needs to be reimplemented to use arrays instead of collections.
    // Data can't be cached when using objects
    public static void main(String[] args) {
        try {
            distances = Util.getDistances(Paths.get(args[0]));
            niWeightMatrix = Util.getNiWeightMatrix(Paths.get(args[0]), BETA);
        } catch (IOException e) {
            System.out.println("Invalid path to a dataset is given.");
            return;
        }
        k = Integer.parseInt(args[1]);
        ants = Integer.parseInt(args[2]);
        maxIter = Integer.parseInt(args[3]);
        initCities();
        fillCities(k);
        a = cities.size() * (cities.size() - 1) / (cities.size() * (-1 + Math.pow(((double) k / cities.size()), - 1. / cities.size())));
        initTau();
        pheromone = new double[niWeightMatrix.length][niWeightMatrix.length];
        resetPheromone();
        run();
    }

    private static void initTau() {
        List<City> trail = new ArrayList<>();
        Set<Integer> visitedIndexes = new HashSet<>();
        while (visitedIndexes.size() != cities.size()) {
            int randomIndex = random.nextInt(cities.size());
            if (visitedIndexes.contains(randomIndex)) {
                continue;
            }
            visitedIndexes.add(randomIndex);
            trail.add(cities.get(randomIndex));
        }
        tauMax = 1 / calculateDistance(trail);
        tauMin = tauMax / a;
    }

    private static void run() {
        double shortestDistance = 0;
        List<City> bestAnt = new ArrayList<>();
        int countSame = 0;
        double oldShortestDistance = 0;
        for (int i = 0; i < maxIter; i++) {
            if ((double) i / maxIter < PERCENTAGE) {
                bestAnt = new ArrayList<>();
                shortestDistance = 0;
            }
            for (int j = 0; j < ants; j++) {
                List<City> trail = runAnt();
                double currentDistance = calculateDistance(trail);
                if (shortestDistance == 0) {
                    shortestDistance = currentDistance;
                    bestAnt = trail;
                    adjustTau(currentDistance);
                } else if (currentDistance < shortestDistance) {
                    adjustTau(currentDistance);
                    shortestDistance = currentDistance;
                    bestAnt = trail;
                }
            }
            if (oldShortestDistance == shortestDistance) {
                countSame++;
            } else {
                oldShortestDistance = shortestDistance;
                countSame = 0;
            }
            if (countSame >= STAGNATION) {
                resetPheromone();
            }
            evaporate();
            applyPheromone(bestAnt, shortestDistance);
            System.out.println(shortestDistance);
        }
        System.out.println(shortestDistance);
        printTrail(bestAnt);
    }

    private static void printTrail(List<City> trail) {
        int i = 0;
        int index = 0;
        boolean once = true;
        while (i < trail.size() - 1) {
            index %= trail.size();
            if ((once) && (cities.get(0) == trail.get(index))) {
                System.out.println(cities.indexOf(trail.get(index)) + 1);
                once = false;
            } else if (!once) {
                System.out.println(cities.indexOf(trail.get(index)) + 1);
                i++;
            }
            index++;
        }
    }

    private static void applyPheromone(List<City> trail, double distance) {
        for (int i = 0; i < trail.size(); i++) {
            int iterationIndex = cities.indexOf(trail.get(i));
            if (i == trail.size() - 1) {
                int firstIndex = cities.indexOf(trail.get(0));
                pheromone[iterationIndex][firstIndex] =
                        Math.min(pheromone[iterationIndex][firstIndex] + (1 / distance), tauMax);
                pheromone[firstIndex][iterationIndex] = pheromone[iterationIndex][firstIndex];
                continue;
            }
            int nextIndex = cities.indexOf(trail.get(i + 1));
            pheromone[iterationIndex][nextIndex] =
                    Math.min(pheromone[iterationIndex][nextIndex] + (1 / distance), tauMax);
            pheromone[nextIndex][iterationIndex] = pheromone[iterationIndex][nextIndex];
        }
    }

    private static void evaporate() {
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = 0; j < pheromone.length; j++) {
                pheromone[i][j] = Math.max(pheromone[i][j] * (1 - RO), tauMin);
                pheromone[j][i] = pheromone[i][j];
            }
        }
    }

    private static void adjustTau(double currentDistance) {
        tauMax = 1 / (RO * currentDistance);
        tauMin = tauMax / a;
    }

    private static List<City> runAnt() {
        List<City> visitedCities = new ArrayList<>();
        City currentCity = cities.get(Math.abs(random.nextInt()) % cities.size());
        visitedCities.add(currentCity);
        for (int i = 0; i < cities.size() - 1; i++) {
            currentCity = pickRandProp(currentCity, currentCity.getClosestCities(), visitedCities);
            visitedCities.add(currentCity);
        }
        return visitedCities;
    }

    private static double calculateDistance(List<City> listOfCities) {
        double distance = 0;
        for (int i = 1; i < listOfCities.size(); i++) {
            if (i == listOfCities.size() - 1) {
                distance += listOfCities.get(i).getDistance(listOfCities.get(0));
                continue;
            }
            distance += listOfCities.get(i).getDistance(listOfCities.get(i + 1));
        }
        return distance;
    }

    private static City pickRandProp(City currentCity, List<City> citiesToVisit, List<City> visitedCities) {
        List<City> candidates = new ArrayList<>();
        for (City city : citiesToVisit) {
            if (!visitedCities.contains(city)) {
                candidates.add(city);
            }
        }
        if (candidates.size() == 0) {
            return pickRandProp(currentCity, cities, visitedCities);
        } else if (candidates.size() == 1) {
            return candidates.get(0);
        }
        double[] chances = new double[candidates.size()];
        double sum = 0;
        int firstIndex = cities.indexOf(currentCity);
        for (int i = 0; i < chances.length; i++) {
            if (i > 0) {
                int secondIndex = cities.indexOf(candidates.get(i));
                chances[i] = Math.pow(pheromone[firstIndex][secondIndex], ALPHA)
                        * niWeightMatrix[firstIndex][secondIndex]
                        + chances[i - 1];
                sum += chances[i] - chances[i - 1];
            }
        }
        for (int i = 0; i < chances.length; i++) {
            chances[i] /= sum;
        }
        double randomNumber = random.nextDouble();
        int selectedParent = -1;
        for (int i = 0; i < chances.length; i++) {
            if (chances[i] >= randomNumber) {
                selectedParent = i;
                break;
            }
        }
        return candidates.get(selectedParent);
    }

    private static void resetPheromone() {
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = 0; j < pheromone.length; j++) {
                pheromone[i][j] = tauMax;
            }
        }
    }

    private static void initCities() {
        cities = new ArrayList<>();
        for (int i = 0; i < niWeightMatrix.length; i++) {
            cities.add(new City());
        }
    }

    private static void fillCities(int k) {
        for (int i = 0; i < niWeightMatrix.length; i++) {
            List<DistanceAndCity> distanceAndCities = new ArrayList<>();
            for (int j = 0; j < niWeightMatrix.length; j++) {
                if (i == j) {
                    continue;
                }
                distanceAndCities.add(new DistanceAndCity(distances[i][j], cities.get(j)));
            }
            distanceAndCities.sort(Comparator.comparing(DistanceAndCity::getDistance, Comparator.reverseOrder()));
            cities.get(i).setClosestCities(distanceAndCities.subList(0, k));
            cities.get(i).setAllCities(distanceAndCities);
        }
    }
}
