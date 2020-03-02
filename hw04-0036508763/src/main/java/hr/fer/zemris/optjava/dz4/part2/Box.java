package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Box {
    private List<Column> sticks;
    private int height;
    private double GOOD_HEIGHT_PERC = 0.95;
    private static double MUTATION_CHANCE = 0.10;
    private static final int MUTATED_STICKS = 5;
    private static Random random = new Random();

    public Box(int height) {
        sticks = new ArrayList<>();
        this.height = height;
    }

    public Box(int height, String[] sticks) {
        this(height);
        for (String stick : sticks) {
            this.sticks.add(new Column(Integer.parseInt(stick)));
        }
    }

    public Box cross(Box other) {
        Box newBox = new Box(height);
        SticksProvider sp = new SticksProvider(this.sticks);

        // adds best columns of both parents
        for (int i = 0; i < Math.max(this.sticks.size(), other.sticks.size()); i++) {
            if ((this.getSticks().size() > i) && (this.sticks.get(i).getFitness() / height > GOOD_HEIGHT_PERC)) {
                if (sp.containsColumn(this.sticks.get(i))) {
                    newBox.addColumn(sp.provide(this.sticks.get(i).clone()));
                }
            }
            if ((other.getSticks().size() > i) && (other.getSticks().get(i).getFitness() / height > GOOD_HEIGHT_PERC)) {
                if (sp.containsColumn(other.getSticks().get(i))) {
                    newBox.addColumn(sp.provide(other.getSticks().get(i).clone()));
                }
            }
        }

        // try filling empty spaces with largest sticks first
        int columnIndex = 0;
        boolean provided = false;

        while (sp.remaining() > 0) {
            int randomIndex = Math.abs(random.nextInt()) % sp.getSticks().size();
            while (sp.remaining() > 0 && newBox.addStickToColumn(sp.peekSticks(randomIndex), columnIndex)) {
                provided = true;
                sp.provideSticks(randomIndex);
                if (sp.getSticks().size() == 0) {
                    break;
                }
                randomIndex = Math.abs(random.nextInt()) % sp.getSticks().size();
            }
            columnIndex++;
            if (columnIndex == this.getSticks().size()) {
                if (!provided) {
                    newBox.addStickToColumn(sp.provideSticks(randomIndex), newBox.getSticks().size());
                }
                provided = false;
                columnIndex %= this.getSticks().size();
            }
        }
        for (int i = 0; i < newBox.getSticks().size(); i++) {
            mutate(newBox);
        }
        Collections.shuffle(newBox.getSticks());
        return newBox;
    }

    public Box clone() {
        Box box = new Box(height);
        List<Column> columns = new ArrayList<>();
        for (Column column : sticks) {
            columns.add(column.clone());
        }
        box.setSticks(columns);
        return box;
    }

    private void mutate(Box box) {
        if (random.nextDouble() > MUTATION_CHANCE) {
            return;
        }
        int columnIndex = Math.abs(random.nextInt()) % box.getSticks().size();
        Column column = box.getSticks().get(columnIndex);
        while (column.getSize() == 1) {
            columnIndex = Math.abs(random.nextInt()) % box.getSticks().size();
            column = box.getSticks().get(columnIndex);
        }
        int stick = column.getAtIndex(Math.abs(random.nextInt()) % column.getSize());
        column.removeStick(stick);
        boolean inserted = false;
        for (int i = 0; i < box.getSticks().size(); i++) {
            int randomIndex = Math.abs(random.nextInt()) % box.getSticks().size();
            if (box.getSticks().get(randomIndex).getFitness() + stick < height) {
                box.getSticks().get(randomIndex).insert(stick);
                inserted = true;
                break;
            }
        }
        if (!inserted) {
            box.addColumn(new Column(stick));
        }
    }

    public double getFitness(int startingSize) {
        double fitness = startingSize - this.sticks.size();
        for (Column column : sticks) {
            fitness += column.getFitness() / height;
        }
        return fitness;
    }

    public boolean addStickToColumn(int stick, int column) {
        if (sticks.size() <= column) {
            sticks.add(new Column(stick));
            return true;
        }

        if (sticks.get(column).getFitness() + stick > height) {
            return false;
        }

        sticks.get(column).insert(stick);
        return true;
    }

    public List<Column> getSticks() {
        return sticks;
    }

    public void setSticks(List<Column> sticks) {
        this.sticks = sticks;
    }

    public void addColumn(Column column) {
        sticks.add(column);
    }
}
