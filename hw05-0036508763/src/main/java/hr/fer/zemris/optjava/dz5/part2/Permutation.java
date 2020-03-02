package hr.fer.zemris.optjava.dz5.part2;

import java.util.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Permutation {
    private List<Integer> factories;

    public Permutation(List<Integer> populations, int start, int end) {
    }

    public Permutation(List<Integer> factories) {
        this.factories = factories;
    }

    public Permutation() {
        factories = new ArrayList<>();
    }

    public List<Integer> getFactories() {
        return factories;
    }

    public int size() {
        return factories.size();
    }

    public void add(int value) {
        this.factories.add(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permutation that = (Permutation) o;
        if (this.size() != that.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.getFactories().get(i).equals(that.getFactories().get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(factories);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        factories.forEach(f -> sb.append(f).append(" "));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
