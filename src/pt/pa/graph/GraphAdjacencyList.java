package pt.pa.graph;

import java.util.*;

/**
 * ADT Graph implementation based on an Adjacency List.
 * <p>
 * It stores a collection of vertices and where
 * each vertex contains the references for the incident edges.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 *
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 */
public class GraphAdjacencyList<V, E> implements Graph<V, E> {

    /* inner classes are defined at the end of the class, so are the auxiliary methods
     */
    private Map<V, Vertex<V>> vertices;

    /**
     * Creates an empty graph.
     */
    public GraphAdjacencyList() {
        this.vertices = new HashMap<>();
    }

    public GraphAdjacencyList(Map<V, Vertex<V>> vertices) {
        this.vertices = vertices;
    }

    /**
     * Checks whether two vertices are adjacent, if exists some
     * edge connecting 'u' and 'v'.
     *
     * @param u first vertex
     * @param v second vertex
     * @return true if they are adjacent, false otherwise.
     * @throws InvalidVertexException if 'u' or 'v' are invalid vertices.
     */
    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        MyVertex myU = checkVertex(u);
        MyVertex myV = checkVertex(v);

        //is there a common edge between myU.incidentEdges and myV.incidentEdges ?

        Set<Edge<E, V>> intersection = new HashSet<>(myU.incidentEdges);
        intersection.retainAll(myV.incidentEdges);

        return !intersection.isEmpty();
    }

    /**
     * Returns the total number of vertices in the graph.
     *
     * @return total number of vertices.
     */
    @Override
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Returns the total number of edges in the graph.
     *
     * @return total number of edges.
     */
    @Override
    public int numEdges() {
        return edges().size();
    }

    /**
     * Returns the vertices in the graph as a Collection.
     * <p>
     * In case there are no vertices, returns an empty Collection.
     *
     * @return Collection of vertices.
     */
    @Override
    public Collection<Vertex<V>> vertices() {
        List<Vertex<V>> vertexList = new ArrayList<>();

        for (Vertex<V> v : vertices.values()) {
            vertexList.add(v);
        }
        return vertexList;
    }

    public Map<V, Vertex<V>> getVertices() {
        return vertices;
    }

    /**
     * Returns the edges in the graph as a Collection.
     * <p>
     * In case there are no edges, returns an empty Collection.
     *
     * @return Collection of edges.
     */
    @Override
    public Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> edgeList = new ArrayList<>();

        for (Vertex<V> v : this.vertices.values()) {
            for (Edge<E, V> e : checkVertex(v).incidentEdges) {
                if (!edgeList.contains(e)) {
                    edgeList.add(e);
                }
            }
        }
        return edgeList;
    }

    /**
     * Returns a vertex's incident edges as a Collection.
     * <p>
     * A vertex is incident with an edge if the vertex is one of the endpoints of that edge.
     * <p>
     * In case there are no incident edges, returns an empty collection.
     *
     * @param v vertex for which to obtain the incident edges
     * @return Collection of edges.
     */
    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        return checkVertex(v).incidentEdges;
    }

    /**
     * Given a vertex 'v', returns the opposite vertex at the other end of an edge 'e'.
     * <p>
     * If both 'v' and 'e' are valid, but 'e' is not connected to 'v', returns null.
     *
     * @param v vertex at one end of 'e'
     * @param e edge connected to 'v'
     * @return opposite vertex at the other end of 'e'.
     * @throws InvalidVertexException if the vertex is invalid.
     * @throws InvalidEdgeException   if the edge is invalid.
     */
    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        MyVertex myVertex = checkVertex(v);
        Vertex<V> tempVertex = null;

        List<Vertex<V>> tempVertexList = new ArrayList<>();

        if (myVertex.incidentEdges.contains(e)) {
            for (Vertex<V> vertex : this.vertices.values()) {
                MyVertex currMyVertex = checkVertex(vertex);
                if (currMyVertex.incidentEdges.contains(e)) {
                    tempVertexList.add(vertex);
                }
            }
        }
        if (tempVertexList.size() > 1) {
            for (Vertex<V> vertex : tempVertexList) {
                if (!vertex.equals(myVertex)) {
                    tempVertex = vertex;
                }
            }
        } else if (tempVertexList.size() == 1) {
            tempVertex = tempVertexList.get(0);
        }

        return tempVertex;
    }

    /**
     * Inserts a new vertex in the collection and returns it's reference.
     *
     * @param vElement the element to store at the vertex
     * @return reference of the new vertex
     * @throws InvalidVertexException if there's already a vertex containing vElement.
     */
    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        //Validations
        if (vertices.containsKey(vElement))
            throw new InvalidVertexException("Element " + vElement + "already exists in the graph.");

        MyVertex v = new MyVertex(vElement);
        vertices.put(vElement, v);
        return v;

    }

    /**
     * Inserts a new edge between two existing vertices and returns it's reference.
     *
     * @param u           a vertex
     * @param v           another vertex
     * @param edgeElement the element to store in the new edge
     * @return reference of the new edge
     * @throws InvalidVertexException if u or v are invalid vertices
     * @throws InvalidEdgeException   if there's already an edge containing edgeElement
     */
    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        return insertEdge(u.element(), v.element(), edgeElement);
    }

    /**
     * Inserts a new edge between two existing vertices and returns it's reference.
     *
     * @param vElement1   a vertex's stored element
     * @param vElement2   another vertex's stored element
     * @param edgeElement the element to store in the new edge
     * @return reference of the new edge
     * @throws InvalidVertexException if u or v are invalid vertices
     * @throws InvalidEdgeException   if there's already an edge containing edgeElement
     */
    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (!(vertices.containsKey(vElement1) || vertices.containsKey(vElement2)) || vElement1 == null || vElement2 == null) {
            throw new InvalidVertexException("One of the vertex does not exist!");
        }

        MyVertex myU = vertexOf(vElement1);
        MyVertex myV = vertexOf(vElement2);

        for (Edge<E, V> edge : incidentEdges(myV)) {

            if (edge.vertices()[0].equals(myU) || edge.vertices()[1].equals(myU)) {
                throw new InvalidEdgeException("This edge already exists");
            }

        }

        for (Edge<E, V> edge : incidentEdges(myU)) {

            if (edge.vertices()[0].equals(myV) || edge.vertices()[1].equals(myV)) {
                throw new InvalidEdgeException("This edge already exists");
            }

        }

        MyEdge edge = new MyEdge(edgeElement);

        myU.incidentEdges.add(edge);
        myV.incidentEdges.add(edge);

        return edge;
    }

    /**
     * Removes a vertex along with its incident edges. Returns the element stored at the removed vertex.
     *
     * @param v vertex to remove
     * @return the element stored at the removed vertex
     * @throws InvalidVertexException if v is an invalid vertex
     */
    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        //Validations
        MyVertex myVertex = checkVertex(v);

        V element = myVertex.element();

        List<Edge<E, V>> edgeList = new ArrayList<>(myVertex.incidentEdges);

        for (Edge<E, V> edge : edgeList) {
            removeEdge(edge);
        }

        vertices.remove(v.element());

        return element;
    }

    /**
     * Removes an edge. Returns its element.
     *
     * @param e edge to remove
     * @return the element stored at the edge
     * @throws InvalidEdgeException if e is an invalid edge
     */
    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        List<Vertex<V>> newVertices = new ArrayList<>();

        if (!existsEdgeWith(e.element())) {
            throw new InvalidEdgeException("This edge doesn't exist.");
        }

        for (Vertex<V> v1 : vertices()) {
            if (checkVertex(v1).incidentEdges.contains(e)) {
                newVertices.add(v1);
            }
        }

        for (Vertex<V> v2 : newVertices) {
            checkVertex(v2).incidentEdges.remove(e);
        }

        E oldElement = e.element();

        return oldElement;
    }

    /**
     * Replaces the element of a given vertex with a new element.
     *
     * @param v          vertex to replace its element
     * @param newElement new element to store in <code>v</code>
     * @return the previous element stored at v
     * @throws InvalidVertexException if there's already exists a vertex containing the element.
     */
    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        //Validations
        if (vertices.containsKey(newElement)) {
            throw new InvalidVertexException("There is already a vertex with this element.");
        }

        List<Edge<E, V>> edges = new ArrayList<>(checkVertex(v).incidentEdges);

        removeVertex(v);

        MyVertex v1 = new MyVertex(newElement);

        v1.incidentEdges = new ArrayList<>(edges);

        vertices.put(newElement, v1);

        return v.element();
    }

    /**
     * Replaces the element of a given edge with a new element.
     *
     * @param e          edge to replace its element
     * @param newElement new element to store in <code>e</code>
     * @return the previous element stored at e
     * @throws InvalidEdgeException if there's already exists an edge containing the element.
     */
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        //Validations
        if (existsEdgeWith(newElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }

        MyEdge edge = checkEdge(e);

        E oldElement = edge.element;

        for (Vertex<V> v1 : this.vertices.values()) {
            if (checkVertex(v1).incidentEdges.contains(e)) {
                edge.element = newElement;
            }

        }

        return oldElement;
    }

    /**
     * Checks if a certain edgeElement exists.
     *
     * @param edgeElement element to check
     * @return true if it exists, otherwise false
     */
    private boolean existsEdgeWith(E edgeElement) {
        for (Vertex<V> v1 : this.vertices.values()) {
            for (Edge<E, V> edge : incidentEdges(v1)) {
                if (edge.element().equals(edgeElement)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Inner class that stores an element of a vertex and its incident edges.
     */
    private class MyVertex implements Vertex<V> {
        private V element;
        private List<Edge<E, V>> incidentEdges;

        public MyVertex(V element) {
            this.element = element;
            this.incidentEdges = new ArrayList<>();
        }

        @Override
        public V element() {
            return element;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}' + " --> " + incidentEdges.toString();
        }
    }

    /**
     * Inner class that stores an element of an edge.
     */
    private class MyEdge implements Edge<E, V> {
        private E element;

        public MyEdge(E element) {
            this.element = element;
        }

        @Override
        public E element() {
            return element;
        }

        @Override
        public Vertex<V>[] vertices() {
            //if the edge exists, then two existing vertices have the edge
            //in their incidentEdges lists
            List<Vertex<V>> adjacentVertices = new ArrayList<>();

            for (Vertex<V> v : GraphAdjacencyList.this.vertices.values()) {
                MyVertex myV = (MyVertex) v;

                if (myV.incidentEdges.contains(this)) {
                    adjacentVertices.add(v);
                }
            }

            if (adjacentVertices.isEmpty()) {
                return new Vertex[]{null, null}; //edge was removed meanwhile
            } else {
                return new Vertex[]{adjacentVertices.get(0), adjacentVertices.get(1)};
            }
        }

        @Override
        public String toString() {
            return "Edge{" + element + "}";
        }
    }

    /**
     * This method will convert a V element into MyVertex element.
     *
     * @param vElement element to transform into MyVertex
     * @return element of type MyVertex if it works or null otherwise
     */
    private MyVertex vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.element().equals(vElement)) {
                return checkVertex(v);
            }
        }
        return null;
    }

    /**
     * Checks whether a given vertex is valid and belongs to the graph
     *
     * @param v vertex to be checked
     * @return MyVertex
     * @throws InvalidVertexException if the vertex is invalid or doesn't exist
     */
    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if (v == null) throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsValue(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    /**
     * Checks whether a given edge is valid and belongs to this graph.
     *
     * @param e edge to be checked
     * @return MyEdge
     * @throws InvalidEdgeException if the edge is invalid or doesn't exist
     */
    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if (e == null) throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidEdgeException("Not an edge.");
        }

        if (!edges().contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    /**
     * Returns the graph in String Format.
     *
     * @return graph in string format
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph | Adjacency List : \n");
        sb.append("Vertices: " + numVertices() + " Edges: " + numEdges() + "\n");

        for (Vertex<V> v : vertices.values()) {
            sb.append(String.format("%s", v));
            sb.append("\n");
        }

        return sb.toString();
    }
}
