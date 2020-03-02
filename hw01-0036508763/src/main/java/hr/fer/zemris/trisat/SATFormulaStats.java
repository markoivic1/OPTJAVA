package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.util.Util;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SATFormulaStats {

    // Za veći broj najboljih riješenja algoritam radi sporije,
    // ali je manja šansa da zapne
    public final static int NUMBER_OF_BEST = 10;
    private static double PERCENTAGE_CONSTANT_UP = 0.01;
    private static double PERCENTAGE_CONSTANT_DOWN = 0.1;
    private static int PERCENTAGE_UNIT_AMOUNT = 50;

    private int satisfiedClauses;
    private double[] post;
    private double percentageBonus;
    private BitVector assignment;
    private SATFormula formula;

    public SATFormulaStats(SATFormula formula) {
        this.formula = formula;
        this.satisfiedClauses = 0;
        this.post = new double[formula.getNumberOfClauses()];
    }

    // analizira se predano rješenje i pamte svi relevantni pokazatelji
    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        satisfiedClauses = Util.getFitness(formula, assignment);
        this.assignment = assignment;

        if (updatePercentages) {
            for (int i = 0; i < assignment.getSize(); i++) {
                if (formula.getClause(i).isSatisfied(assignment)) {
                    post[i] += (1 - post[i]) * PERCENTAGE_CONSTANT_UP;
                } else {
                    post[i] += (0 - post[i]) * PERCENTAGE_CONSTANT_DOWN;
                }
            }
        }

    }

    // vraća temeljem onoga što je setAssignment zapamtio: broj klauzula koje su zadovoljene
    public int getNumberOfSatisfied() {
        return satisfiedClauses;
    }

    // vraća temeljem onoga što je setAssignment zapamtio
    public boolean isSatisfied() {
        return satisfiedClauses == formula.getNumberOfClauses();
    }

    // vraća temeljem onoga što je setAssignment zapamtio: suma korekcija klauzula
    public double getPercentageBonus() {
        percentageBonus = getNumberOfSatisfied();
        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            if (formula.getClause(i).isSatisfied(assignment)) {
                percentageBonus += PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
            } else {
                percentageBonus += -1 * PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
            }
        }
        return percentageBonus;
    }

    // vraća temeljem onoga što je setAssignment zapamtio: procjena postotka za klauzulu
    public double getPercentage(int index) {
        return post[index];
    }
}
