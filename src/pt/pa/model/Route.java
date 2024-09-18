package pt.pa.model;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;

/**
 * Represents a route (edge) in the graph.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class Route {
    private static int nsxtIdentifier = 1;
    private final int identifier;
    private int distance;

    /**
     * Route class Constructor.
     *
     * @param distance - Int
     */
    public Route(int distance) {
        this.distance = distance;
        this.identifier = nsxtIdentifier;
        nsxtIdentifier++;
    }

    /**
     * Getter - Distance.
     *
     * @return int - distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Setter - Distance.
     *
     * @param distance - Int
     */
    public void setDistance(int distance) {
        this.distance = distance;
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
        Route route = (Route) o;
        return identifier == route.identifier && distance == route.distance;
    }

    /**
     * Returns the distance.
     *
     * @return String - distance
     */
    @SmartLabelSource
    public String getDisplayDistance() {
        return distance + " km";
    }

    /**
     * toString method - returns the identifier and the distance.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Route{" +
                "identifier=" + identifier +
                ", distance=" + distance +
                '}';
    }
}