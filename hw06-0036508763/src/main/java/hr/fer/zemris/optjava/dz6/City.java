package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class City {
    private List<DistanceAndCity> closestCities;
    private List<DistanceAndCity> allCities;

    public City() {
        this.closestCities = new ArrayList<>();
        this.allCities = new ArrayList<>();
    }

    public void addCity(DistanceAndCity city) {
        closestCities.add(city);
    }

    public double getDistance(City city) {
        if (!getAllCities().contains(city)) {
            return -1;
        }
        return allCities.get(getAllCities().indexOf(city)).getDistance();
    }

    public List<City> getClosestCities() {
        List<City> cities = new ArrayList<>();
        for (DistanceAndCity city : closestCities) {
            cities.add(city.getCity());
        }
        return cities;
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        for (DistanceAndCity city : allCities) {
            cities.add(city.getCity());
        }
        return cities;
    }

    public void setClosestCities(List<DistanceAndCity> closestCities) {
        this.closestCities = closestCities;
    }


    public void setAllCities(List<DistanceAndCity> allCities) {
        this.allCities = allCities;
    }
}
