package pt.pa.graph;

import com.brunomnsilva.smartgraph.example.City;
import com.brunomnsilva.smartgraph.example.Distance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.model.Coordinate;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class with Unit Tests.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
class GraphAdjacencyListTest {

    Graph<Hub, Route> g;
    Vertex<Hub> lisboa;
    Vertex<Hub> porto;
    Vertex<Hub> coimbra;
    Vertex<Hub> setubal;
    Vertex<Hub> faro;
    Vertex<Hub> barreiro;

    Edge<Route, Hub> routeHubEdge;

    @BeforeEach
    void setUp() {

        g = new GraphAdjacencyList<>();

        lisboa = g.insertVertex(new Hub("lisboa", 1000, new Coordinate(1000, 500)));
        porto = g.insertVertex(new Hub("porto", 900, new Coordinate(950, 800)));
        coimbra = g.insertVertex(new Hub("coimbra", 800, new Coordinate(975, 600)));
        setubal = g.insertVertex(new Hub("setubal", 700, new Coordinate(990, 450)));
        faro = g.insertVertex(new Hub("faro", 600, new Coordinate(750, 200)));
        barreiro = g.insertVertex(new Hub("barreiro", 500, new Coordinate(1, 1)));

        routeHubEdge = g.insertEdge(lisboa, coimbra, new Route(7000));
        g.insertEdge(lisboa, porto, new Route(11550));
        g.insertEdge(porto, coimbra, new Route(1303));
        g.insertEdge(coimbra, setubal, new Route(5567));
        g.insertEdge(setubal, lisboa, new Route(1264));
        g.insertEdge(barreiro, lisboa, new Route(7815));
        g.insertEdge(setubal, faro, new Route(1845));
        g.insertEdge(faro, porto, new Route(8132));
    }


    @Test
    void areAdjacent() {
        assertTrue(g.areAdjacent(lisboa, porto));
        assertFalse(g.areAdjacent(lisboa, faro));
    }

    @Test
    void numVertices() {
        assertEquals(6, g.numVertices());
        Vertex<Hub> test = g.insertVertex(new Hub("test", 1, new Coordinate(1, 1)));
        assertEquals(7, g.numVertices());
        g.removeVertex(test);
        assertEquals(6, g.numVertices());
    }

    @Test
    void numEdges() {
        assertEquals(8, g.numEdges());
        Edge<Route, Hub> test = g.insertEdge(faro, lisboa, new Route(8132));
        assertEquals(9, g.numEdges());
        g.removeEdge(test);
        assertEquals(8, g.numEdges());
    }

    @Test
    void vertices() {
        Collection<Vertex<Hub>> list = g.vertices();
        assertEquals(list, g.vertices());
        Vertex<Hub> test = null;
        list.add(test);
        assertNotEquals(list, g.vertices());
    }

    @Test
    void edges() {
        Collection<Edge<Route, Hub>> list = g.edges();
        assertEquals(list, g.edges());
        Edge<Route, Hub> test = null;
        list.add(test);
        assertNotEquals(list, g.edges());
    }

    @Test
    void incidentEdges() {
        Collection<Edge<Route, Hub>> list = g.incidentEdges(lisboa);
        assertEquals(list, g.incidentEdges(lisboa));
        assertNotEquals(list, g.incidentEdges(setubal));
    }

    @Test
    void opposite() {
        Vertex<Hub> test = g.opposite(lisboa, routeHubEdge);
        assertEquals("coimbra", test.element().getCity());
        assertNotEquals(test, g.opposite(setubal, routeHubEdge));
    }

    @Test
    void insertVertex() {
        Vertex<Hub> test = g.insertVertex(new Hub("Test", 1, new Coordinate(1, 1)));
        assertEquals("Test", test.element().getCity());
        assertThrows(InvalidVertexException.class, () -> {
            g.insertVertex(test.element());
        });
    }

    @Test
    void insertEdge() {
        Graph<Hub, Route> graphTest = new GraphAdjacencyList<>();
        Vertex<Hub> testV = graphTest.insertVertex(new Hub("test", 1, new Coordinate(1, 1)));

        Edge<Route, Hub> test = g.insertEdge(faro, lisboa, new Route(8132));
        assertThrows(InvalidVertexException.class, () -> {
            g.insertEdge(lisboa, testV, new Route(8132));
        });
        assertThrows(InvalidEdgeException.class, () -> {
            g.insertEdge(lisboa, porto, new Route(8132));
        });
    }

    @Test
    void removeVertex() {
        Graph<Hub, Route> graphTest = new GraphAdjacencyList<>();
        assertThrows(InvalidVertexException.class, () -> {
            graphTest.removeVertex(lisboa);
        });
        assertEquals("lisboa", g.removeVertex(lisboa).getCity());
    }

    @Test
    void removeEdge() {
        Graph<Hub, Route> graphTest = new GraphAdjacencyList<>();
        assertThrows(InvalidEdgeException.class, () -> {
            graphTest.removeEdge(routeHubEdge);
        });
        assertEquals("7000 km", String.valueOf(g.removeEdge(routeHubEdge).getDisplayDistance()));
    }

    @Test
    void replaceVertex() {
        assertThrows(InvalidVertexException.class, () -> {
            g.replace(lisboa, porto.element());
        });
        Hub test = new Hub("test", 900, new Coordinate(950, 800));
        assertEquals(lisboa.element(), g.replace(lisboa, test));
    }

    @Test
    void replaceEdge() {
        assertThrows(InvalidEdgeException.class, () -> {
            g.replace(routeHubEdge, routeHubEdge.element());
        });
        Route test = new Route(1000);
        assertEquals(routeHubEdge.element(), g.replace(routeHubEdge, test));
    }

    @Test
    void testToString() {
        assertTrue(!g.toString().isEmpty());
    }
}