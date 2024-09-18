package pt.pa.utils;

import pt.pa.graph.Edge;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.Vertex;
import pt.pa.model.Hub;
import pt.pa.model.Route;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class that exports graph's data.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class GraphExportManipulation {
    private String directory;
    private File fileDirectory;
    private Graph<Hub, Route> graph;

    public GraphExportManipulation(String directory, Graph<Hub, Route> g) {
        this.directory = directory;
        fileDirectory = new File(directory + "/sgb-copy");
        if (!fileDirectory.exists()) {
            fileDirectory.mkdir();
        }
        this.graph = g;
    }

    public void setFileName() throws IOException {
        File file = new File(fileDirectory + "/name.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for (Vertex<Hub> vHub : graph.vertices()) {
            bw.write(vHub.element().getCity());
            bw.newLine();
        }
        bw.close();
    }

    public void setFileRoutes() throws IOException {
        File file = new File(fileDirectory + "/routes.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for (Vertex<Hub> vHub1 : graph.vertices()) {
            for (Vertex<Hub> vHub2 : graph.vertices()) {
                if (vHub1 == vHub2) {
                    bw.write("0 ");
                }
                if (graph.areAdjacent(vHub1, vHub2)) {
                    for (Edge<Route, Hub> edge : graph.incidentEdges(vHub2)) {
                        if (vHub1.equals(graph.opposite(vHub2, edge))) {
                            bw.write(String.valueOf(edge.element().getDistance()) + " ");
                        }
                    }
                } else {
                    bw.write("0 ");
                }
            }
            bw.newLine();
        }
        bw.close();
    }

    public void setFileWeight() throws IOException {
        File file = new File(fileDirectory + "/weight.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for (Vertex<Hub> vHub : graph.vertices()) {
            bw.write(String.valueOf(vHub.element().getPopulation()));
            bw.newLine();
        }
        bw.close();
    }

    public void setFileCoords() throws IOException {
        File file = new File(fileDirectory + "/xy.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for (Vertex<Hub> vHub : graph.vertices()) {
            bw.write(vHub.element().getCoordinates().getX() + " " + vHub.element().getCoordinates().getY());
            bw.newLine();
        }
        bw.close();
    }
}
