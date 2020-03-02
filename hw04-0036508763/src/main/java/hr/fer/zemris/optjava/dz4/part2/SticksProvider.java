package hr.fer.zemris.optjava.dz4.part2;

import java.util.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class SticksProvider {
    private List<Integer> sticks;
    private boolean sorted;
    private Random random;

    public SticksProvider(List<Column> sticks) {
        random = new Random();
        this.sticks = new ArrayList<>();
        for (Column column : sticks) {
            this.sticks.addAll(column.getColumn());
        }
        sorted = false;
    }

    public Integer provide(Integer stick) {
        if (!containsStick(stick)) {
            return -1;
        }
        sticks.remove(stick);
        sorted = false;
        return stick;
    }

    public Column provide(Column column) {
        for (int stick : column.getColumn()) {
            provide(stick);
        }
        sorted = false;
        return column;
    }

    public int remaining() {
        return sticks.size();
    }

    public boolean containsColumn(Column column) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer stick : sticks) {
            map.putIfAbsent(stick, 0);
            map.compute(stick, (k, v) -> v = v + 1);
        }
        for (int stick : column.getColumn()) {
            map.putIfAbsent(stick, 0);
            if (map.get(stick) <= 0) {
                return false;
            }
            map.compute(stick, (k, v) -> v = v - 1);
        }
        return true;
    }

    public boolean containsStick(Integer stick) {
        return sticks.contains(stick);
    }

    public int provideSticks(int index) {
        if (index > this.sticks.size()) {
            return -1;
        }
        Integer stick = sticks.get(index);
        sticks.remove(index);
        return stick;
    }

    public int peekSticks(int index) {
        if (index > this.sticks.size()) {
            return -1;
        }
        return sticks.get(index);
    }

    public List<Integer> getSticks() {
        return sticks;
    }
}
