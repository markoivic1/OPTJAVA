package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Column {

    private List<Integer> column;

    public Column(List<Integer> column) {
        this.column = column;
    }

    public Column() {
        column = new ArrayList<>();
    }

    public Column(int stick) {
        this();
        column.add(stick);
    }

    public Column(String[] sticks) {
        this();
        for (String stick : sticks) {
            column.add(Integer.parseInt(stick));
        }
    }

    public Column clone() {
        List<Integer> newData = new ArrayList<>(this.getColumn());
        return new Column(newData);
    }

    public void removeStick(Integer stick) {
        column.remove(stick);
    }

    public double getFitness() {
        double sum = 0;
        for (Integer stick : column) {
            sum += stick;
        }
        return sum;
        // program runs 10s longer when using stream api :(
        // return column.stream().mapToInt(Integer::intValue).sum();
    }

    public int getSize() {
        return column.size();
    }

    public int getAtIndex(int index) {
        return column.get(index);
    }

    public void insert(int stickSize) {
        column.add(stickSize);
    }

    public void insert(List<Integer> column) {
        this.column.addAll(column);
    }

    public List<Integer> getColumn() {
        return column;
    }
}
