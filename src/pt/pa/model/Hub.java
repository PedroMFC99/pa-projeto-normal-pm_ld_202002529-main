package pt.pa.model;

import java.util.Objects;

/**
 * Represents an hub (vertex) in the graph.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class Hub {
    private static int nsxtIdentifier = 1;
    private final int identifier;
    private final String city;
    private final int population;
    private final Coordinate coordinate;

    /**
     * Class Constructor - Initializes an hub.
     *
     * @param city        - String
     * @param population  - Int
     * @param coordinates - Int
     */
    public Hub(String city, int population, Coordinate coordinates) {
        this.city = city;
        this.population = population;
        this.coordinate = coordinates;
        this.identifier = nsxtIdentifier;
        nsxtIdentifier++;
    }

    @Override
    public String toString() {
        return "Hub{" +
                "identifier=" + identifier +
                ", city='" + city + '\'' +
                ", population=" + population +
                ", coordinates= X: " + coordinate.getX() + " Y: " + coordinate.getY() +
                "}";
    }

    /**
     * Method to display a hub's info.
     *
     * @return String - hub's info.
     */


    public int getIdentifier() {
        return identifier;
    }

    /**
     * Getter - City
     *
     * @return String - city
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter - Population
     *
     * @return int - Population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Getter - Coordinates
     *
     * @return Coordinate - coordinates
     */
    public Coordinate getCoordinates() {
        return coordinate;
    }


    /**
     * Equals method (compares two objects).
     *
     * @param o object to compare
     * @return true if objects are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hub hub = (Hub) o;
        return identifier == hub.identifier && population == hub.population && Objects.equals(city, hub.city) && Objects.equals(coordinate, hub.coordinate);
    }

    /**
     * Hashcode method (returns an object hash).
     *
     * @return int - hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(identifier, city, population, coordinate);
    }
}