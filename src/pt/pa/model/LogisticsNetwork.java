package pt.pa.model;

import pt.pa.graph.*;
import pt.pa.observerpattern.Subject;
import pt.pa.utils.DataSetManipulation;
import pt.pa.utils.GraphExportManipulation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents the 'Model' component in the MVC pattern.
 * It is essentially the back-end of the application.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class LogisticsNetwork extends Subject implements Serializable, Originator {
    private Graph<Hub, Route> graph;
    private static final int NULL_VALUES = -1; // Magic Number Fix

    public LogisticsNetwork() {
        this.graph = new GraphAdjacencyList();
    }

    /**
     * Get graph
     *
     * @return graph of the program
     */
    public Graph<Hub, Route> getGraph() {
        return graph;
    }

    /**
     * Calculate the minimum-cost path between two vertices using their IDs.
     *
     * @param firstId  id of the first vertex (origin)
     * @param secondId id of the second vertex (destination)
     * @param path     List of hubs
     * @return the minimum cost between two vertices
     */
    public int minimumCostPath(int firstId, int secondId, List<Hub> path) {
        try {
            Vertex<Hub> vertexOrig = findLocal(firstId);
            Vertex<Hub> vertexDestination = findLocal(secondId);

            if (vertexOrig == null || vertexDestination == null || path == null) return NULL_VALUES;

            Map<Vertex<Hub>, Integer> costs = new HashMap<>();
            Map<Vertex<Hub>, Vertex<Hub>> predecessors = new HashMap<>();

            dijkstra(vertexOrig, costs, predecessors);
            path.clear();
            boolean complete = true;
            Vertex<Hub> actual = vertexDestination;

            while (actual != vertexOrig) {
                path.add(0, actual.element());
                actual = predecessors.get(actual);
                if (actual == null) {
                    complete = false;
                    break;
                }
            }
            path.add(0, vertexOrig.element());
            if (!complete) {
                path.clear();
                return -1;
            }

            return costs.get(vertexDestination).intValue();

        } catch (LogisticsNetworkException e) {
            throw new LogisticsNetworkException();
        }
    }

    /**
     * Calculates the longest minimum cost path between hubs.
     *
     * @param path Path to calculate
     * @return longest minimum cost path
     */
    public int longestMinCostPath(List<Hub> path) {
        Vertex<Hub> vertexOrigin = null;
        Vertex<Hub> vertexDestiny = null;

        Map<Vertex<Hub>, Integer> costs = new HashMap<>();
        Map<Vertex<Hub>, Vertex<Hub>> predecessors = new HashMap<>();

        int maxDistCost = 0;

        for (Vertex<Hub> vHub : graph.vertices()) {
            Vertex<Hub> vD = null;
            vD = dfs(vHub);
            dijkstra(vHub, costs, predecessors);

            int distCost = 0;

            distCost += costs.get(vD).intValue();
            if (distCost > maxDistCost) {
                vertexDestiny = vD;
                vertexOrigin = vHub;
                maxDistCost = distCost;
            }
        }


        if (vertexOrigin == null || vertexDestiny == null || path == null) return NULL_VALUES;

        dijkstra(vertexOrigin, costs, predecessors);
        path.clear();
        boolean complete = true;
        Vertex<Hub> actual = vertexDestiny;

        while (actual != vertexOrigin) {
            path.add(0, actual.element());
            actual = predecessors.get(actual);
            if (actual == null) {
                complete = false;
                break;
            }
        }
        path.add(0, vertexOrigin.element());
        if (!complete) {
            path.clear();
            return NULL_VALUES;
        }

        return costs.get(vertexDestiny).intValue();
    }

    /**
     * Find a local vertex.
     *
     * @param localId localId to find
     * @return Vertex if exists, null otherwise
     */
    public Vertex<Hub> findLocal(int localId) {
        if (localId < 0) return null;
        for (Vertex<Hub> v : graph.vertices()) {
            if (v.element().getIdentifier() == localId) { //equals was overriden in Hub!!
                return v;
            }
        }
        return null;
    }

    /**
     * Finds the furthest vertex from an certain vertex.
     *
     * @param root first vertex
     * @return the furthest vertex from root
     */
    private Vertex<Hub> dfs(Vertex<Hub> root) {
        List<Vertex<Hub>> visited = new ArrayList<>();
        Stack<Vertex<Hub>> stack = new Stack<>();

        Vertex<Hub> furthest = null;

        visited.add(root);
        stack.push(root);

        while (!stack.isEmpty()) {
            Vertex<Hub> vHub = stack.pop();
            furthest = vHub;
            for (Edge<Route, Hub> edge : graph.incidentEdges(vHub)) {
                Vertex<Hub> vHub2 = graph.opposite(vHub, edge);
                if (!visited.contains(vHub2)) {
                    visited.add(vHub2);
                    stack.push(vHub2);
                }
            }
        }
        return furthest;
    }

    /**
     * Applies the dijkstra algorithm.
     *
     * @param origin       origin vertex where the algorithm starts.
     * @param costs        map with the cost of each vertex.
     * @param predecessors map with the predecessors of each vertex
     */
    private void dijkstra(Vertex<Hub> origin,
                          Map<Vertex<Hub>, Integer> costs,
                          Map<Vertex<Hub>, Vertex<Hub>> predecessors) {
        costs.clear();
        predecessors.clear();

        List<Vertex<Hub>> unvisited = new ArrayList<>();

        for (Vertex<Hub> v : graph.vertices()) {
            if (graph.incidentEdges(v) != null) {
                costs.put(v, Integer.MAX_VALUE);
                predecessors.put(v, null);
                unvisited.add(v);
            }
        }
        costs.put(origin, 0);

        while (!unvisited.isEmpty()) {
            Vertex<Hub> lowerCostVertex = findLowerCostVertex(unvisited, costs);
            if (lowerCostVertex == null) {
                break;
            } else {
                unvisited.remove(lowerCostVertex);

                for (Edge<Route, Hub> incidentEdges : graph.incidentEdges(lowerCostVertex)) {
                    Vertex<Hub> opposite = graph.opposite(lowerCostVertex, incidentEdges);

                    if (unvisited.contains(opposite)) {
                        int pathCost = costs.get(lowerCostVertex) + incidentEdges.element().getDistance();
                        if (pathCost < costs.get(opposite)) {
                            costs.put(opposite, pathCost);
                            predecessors.put(opposite, lowerCostVertex);
                        }
                    }
                }
            }
        }
    }

    //private void DFS(Graph<>)

    /**
     * Find the lower cost vertex from a list of unvisited vertices and its costs.
     *
     * @param unvisited List of unvisited vertices.
     * @param costs     Of each vertex.
     * @return vertex with the lower cost.
     */
    private Vertex<Hub> findLowerCostVertex(List<Vertex<Hub>> unvisited,
                                            Map<Vertex<Hub>, Integer> costs) {

        int minimum = Integer.MAX_VALUE;
        Vertex<Hub> lowerCostVertex = null;

        for (Vertex<Hub> v : unvisited) {
            int cost = costs.get(v);
            if (cost < minimum) {
                minimum = cost;
                lowerCostVertex = v;
            }
        }

        return lowerCostVertex;

    }


    /**
     * Returns the vertex related to a hub.
     *
     * @param hub Hub
     * @return vertex if exists
     */
    public Vertex<Hub> returnVertex(Hub hub) {
        for (Vertex<Hub> v : this.graph.vertices()) {
            if (v.element().getIdentifier() == hub.getIdentifier() && v.element().getCity().equals(hub.getCity())) {
                return v;
            }
        }
        return null;
    }

    /**
     * Loads the default data.
     *
     * @param graph Graph to load
     * @throws LogisticsNetworkException if it loading goes wrong
     */
    public void globalDataLoad(Graph<Hub, Route> graph) throws LogisticsNetworkException {
        if (graph == null) {
            throw new LogisticsNetworkException("The graph is empty!");
        }

        try {
            DataSetManipulation dataSetManipulation = new DataSetManipulation("sgb5");
            dataSetManipulation.insertHubsFromDirectory(graph);
            dataSetManipulation.insertRoutesFromDirectory(graph);
            notifyObservers(null);
        } catch (FileNotFoundException e) {
            throw new LogisticsNetworkException();
        }

    }

    /**
     * Makes a data export to a new folder.
     *
     * @param graph Graph
     * @throws LogisticsNetworkException if the data export fails
     */
    public void globalDataExport(Graph<Hub, Route> graph) throws LogisticsNetworkException {
        if (graph == null) {
            throw new LogisticsNetworkException("The graph is empty!");
        }

        try {
            GraphExportManipulation graphExportManipulation = new GraphExportManipulation("dataset", graph);
            graphExportManipulation.setFileName();
            graphExportManipulation.setFileRoutes();
            graphExportManipulation.setFileWeight();
            graphExportManipulation.setFileCoords();
            notifyObservers(null);
        } catch (IOException e) {
            throw new LogisticsNetworkException();
        }
    }

    /**
     * Get number of hubs.
     *
     * @return number of hubs
     * @throws LogisticsNetworkException if an error occurs
     */
    public int getNumberOfHubs() throws LogisticsNetworkException {
        return graph.numVertices();
    }

    /**
     * Get number of routes.
     *
     * @return number of routes
     * @throws LogisticsNetworkException if an error occurs
     */
    public int getNumberOfRoutes() throws LogisticsNetworkException {
        return graph.numEdges();
    }

    /**
     * Adds a route between two hubs.
     *
     * @param distance String
     * @param idHub1   int
     * @param idHub2   int
     * @throws LogisticsNetworkException if one of the hubs is null
     */
    public void addRoute(String distance, int idHub1, int idHub2) throws LogisticsNetworkException {
        try {
            Vertex<Hub> h1 = findHub(idHub1);
            Vertex<Hub> h2 = findHub(idHub2);
            if (h1 == null) {
                throw new LogisticsNetworkException(" id " + idHub1 + " not exist");
            }
            if (h2 == null) {
                throw new LogisticsNetworkException(" id " + idHub2 + " not exist");
            }

            Route route = new Route(Integer.parseInt(distance));

            graph.insertEdge(h1, h2, route);

            notifyObservers(null);

        } catch (InvalidVertexException e) {
            throw new LogisticsNetworkException();
        }
    }

    /**
     * Removes a route between two hubs.
     *
     * @param id1 int
     * @param id2 int
     * @throws LogisticsNetworkException if one of the hubs is null
     */
    public void removeRoute(int id1, int id2) throws LogisticsNetworkException {
        try {
            Vertex<Hub> h1 = findHub(id1);
            Vertex<Hub> h2 = findHub(id2);

            if (h1 == null) {
                throw new LogisticsNetworkException(" id " + id1 + " not exist");
            }
            if (h2 == null) {
                throw new LogisticsNetworkException(" id " + id2 + " not exist");
            }

            Edge<Route, Hub> tempEdge = null;

            for (Edge<Route, Hub> edge : graph.incidentEdges(h1)) {
                if (graph.opposite(h1, edge) == h2) {
                    tempEdge = edge;
                }
            }
            graph.removeEdge(tempEdge);

            notifyObservers(null);

        } catch (InvalidVertexException e) {
            throw new LogisticsNetworkException();
        }
    }

    /**
     * Returns a collection of hubs.
     *
     * @return Collection of hubs
     */
    public Collection<Hub> getHubs() {
        List<Hub> hubs = new ArrayList<>();

        for (Vertex<Hub> v : graph.vertices()) {
            hubs.add(v.element());
        }

        return hubs;
    }

    /**
     * Find a vertex(hub) based on an id.
     *
     * @param id int
     * @return vertex(hub)
     */
    private Vertex<Hub> findHub(int id) {
        for (Vertex<Hub> v : graph.vertices()) {
            if (v.element().getIdentifier() == id) {
                return v;
            }
        }
        return null;
    }

    /**
     * Get a map with the most central hubs by descending order.
     *
     * @return map
     */
    private Map<Hub, Integer> getCentralizedHubsDesc() {

        List<Hub> listHubs = (List<Hub>) getHubs();

        Map<Hub, Integer> mapAux = new HashMap<>();

        for (Hub hub : listHubs) {
            Vertex<Hub> vertex;

            vertex = findHub(hub.getIdentifier());

            int count = 0;

            for (Edge<Route, Hub> edge : this.graph.incidentEdges(vertex)) {
                count++;
            }
            mapAux.put(hub, count);
        }
        return sortDescbyValue(mapAux);
    }

    /**
     * Sorts a map by descending order.
     *
     * @param map to be sorted
     * @return sorted map
     */
    private Map<Hub, Integer> sortDescbyValue(Map<Hub, Integer> map) {
        Map<Hub, Integer> reverseSortedMap = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        return reverseSortedMap;
    }

    /**
     * Returns the most central hubs in string format.
     *
     * @return the most central hubs in string format
     */
    public String getCentralizedHubsText() {
        Map<Hub, Integer> map = getCentralizedHubsDesc();

        StringBuilder sb = new StringBuilder();

        for (Hub hub : map.keySet()) {
            int value = map.get(hub);

            sb.append("Hub " + hub.getIdentifier());
            sb.append("City " + hub.getCity());
            sb.append("Population " + hub.getPopulation());
            sb.append("Coordinates " + hub.getCoordinates().getX());
            sb.append("," + hub.getCoordinates().getY());
            sb.append(" Total Adjacent Hubs: " + value + "\n");
        }
        return sb.toString();
    }

    /**
     * Get a map with the 'top 5' most central hubs.
     *
     * @return map with the 'top 5' most central hubs.
     */
    public Map<Hub, Integer> getTop5CentralizedHubs() {
        Map<Hub, Integer> map = getCentralizedHubsDesc();
        Map<Hub, Integer> mapTopFive = new HashMap<>();
        final int TOP_FIVE = 5;

        int count = 0;

        for (Hub hub : map.keySet()) {
            int value = map.get(hub);
            if (count < TOP_FIVE) {
                mapTopFive.put(hub, value);
            }
            count++;
        }

        return sortDescbyValue(mapTopFive);
    }

    /**
     * Get a list with the 'top 5' most central hubs.
     *
     * @return list with the 'top 5' most central hubs
     */
    public List<String> getTop5CentralizedHubsList() {
        List<String> list = new ArrayList<>();
        LinkedHashMap<Hub, Integer> mapAux = new LinkedHashMap<>(getTop5CentralizedHubs());

        for (Hub h : mapAux.keySet()) {
            list.add(mapAux.get(h).toString());
        }
        for (Hub h : mapAux.keySet()) {
            String hubInfo = String.valueOf(h.getIdentifier()) + " | " + h.getCity();
            list.add(hubInfo);
        }

        return list;
    }

    /**
     * Calculate the number of sub-graphs in the Graph.
     *
     * @return number of sub-graphs in the Graph
     */
    public int getSubGrapthCount() {
        int counter = 0;
        Stack<Vertex<Hub>> stack = new Stack<>();
        List<Vertex<Hub>> visited = new ArrayList<>();

        for (Vertex<Hub> vAux : graph.vertices()) {
            visited.add(vAux);
            stack.push(vAux);

            while (!stack.isEmpty()) {
                Vertex<Hub> v = stack.pop();
                if (graph.incidentEdges(v).isEmpty())
                    counter++;
                for (Edge<Route, Hub> e : graph.incidentEdges(v)) {
                    Vertex<Hub> w = graph.opposite(v, e);
                    if (!visited.contains(w)) {
                        counter++;
                        visited.add(w);
                        stack.push(w);
                    }
                }
            }
        }
        return counter;
    }

    /**
     * Creates a memento (snapshot).
     *
     * @return memento
     */
    @Override
    public Memento createMemento() {
        return new LogisticsNetworkMemento(this.graph.getVertices());
    }

    /**
     * Sets a specific memento (snapshot).
     *
     * @param savedState savedState
     */
    @Override
    public void setMemento(Memento savedState) {
        if (savedState instanceof LogisticsNetworkMemento) {

            getGraph().getVertices().clear();
            getGraph().getVertices().putAll(((LogisticsNetworkMemento) savedState).state);
        }
    }

    /**
     * Inner class that implements Memento.
     */
    private class LogisticsNetworkMemento implements Memento {
        private Map<Hub, Vertex<Hub>> state;

        public LogisticsNetworkMemento(Map<Hub, Vertex<Hub>> state) {
            this.state = new HashMap<>(state);
        }

        @Override
        public String getDescription() {
            return state.toString();
        }
    }
}
