package hr.fer.zemris.optjava.dz10;

import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SecondMOOPProblem implements MOOPProblem {
    @Override
    public int getNumberOfObjectives() {
        return 2;
    }

    @Override
    public void evaluateSolution(double[] solution, double[] objectives) {
        objectives[0] = solution[0];
        objectives[1] = (1 + solution[1]) / solution[0];
    }

    @Override
    public double[] evaluateSolution(double[] solution) {
        double[] objectives = new double[getNumberOfObjectives()];
        evaluateSolution(solution, objectives);
        return objectives;
    }

    @Override
    public void limit(double[] solution) {
        if (solution[0] < 0.1) {
            solution[0] = 0.1;
        } else if (solution[0] > 1) {
            solution[0] = 1;
        }

        if (solution[1] < 0) {
            solution[1] = 0;
        } else if (solution[1] > 5) {
            solution[1] = 5;
        }
    }

    @Override
    public double[] generate(Random random) {
        double[] unit = new double[2];
        unit[0] = random.nextDouble() * 0.9 + 0.1;
        unit[1] = random.nextDouble() * 5;
        return unit;
    }
}
