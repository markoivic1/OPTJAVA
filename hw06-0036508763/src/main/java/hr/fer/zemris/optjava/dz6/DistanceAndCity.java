package hr.fer.zemris.optjava.dz6;

import java.util.Objects;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DistanceAndCity implements Comparable<DistanceAndCity> {
    private double distance;
    private City city;

    public DistanceAndCity(double distance, City city) {
        this.distance = distance;
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(city);
    }

    @Override
    public int compareTo(DistanceAndCity o) {
        return Double.compare(distance, o.distance);
    }
}
