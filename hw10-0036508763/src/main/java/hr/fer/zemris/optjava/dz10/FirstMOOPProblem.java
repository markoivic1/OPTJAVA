package hr.fer.zemris.optjava.dz10;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class FirstMOOPProblem implements MOOPProblem {
    @Override
    public int getNumberOfObjectives() {
        return 4;
    }

    @Override
    public void limit(double[] solution) {
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] < -5) {
                solution[i] = -5;
            } else if (solution[i] > 5) {
                solution[i] = 5;
            }
        }
    }

    @Override
    public double[] evaluateSolution(double[] solution) {
        double[] objectives = new double[getNumberOfObjectives()];
        evaluateSolution(solution, objectives);
        return objectives;
    }

    @Override
    public void evaluateSolution(double[] solution, double[] objectives) {
        for (int i = 0; i < 4; i++) {
            objectives[i] = solution[i] * solution[i];
        }
    }

    @Override
    public double[] generate(Random random) {
        double[] unit = new double[getNumberOfObjectives()];
        for (int i = 0; i < unit.length; i++) {
            unit[i] = random.nextDouble() * 10 - 5;
        }
        return unit;
    }
}
