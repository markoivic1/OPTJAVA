package hr.fer.zemris.optjava.dz3.solutions;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SingleObjectiveSolution{
    private double fitness;
    private double value;

    public SingleObjectiveSolution() {
    }


    public int compareTo(SingleObjectiveSolution o) {
        return Double.compare(this.fitness, o.fitness);
    }

    public double getFitness() {
        return fitness;
    }

    public double getValue() {
        return value;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
