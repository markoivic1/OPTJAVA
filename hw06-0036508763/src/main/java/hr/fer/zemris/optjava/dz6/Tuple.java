package hr.fer.zemris.optjava.dz6;

import java.util.Objects;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Tuple {
    private double x;
    private double y;

    public Tuple(double[] tuple) {
        x = tuple[0];
        y = tuple[1];
    }

    public Tuple() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Double.compare(tuple.x, x) == 0 &&
                Double.compare(tuple.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
