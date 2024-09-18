package pt.pa.controller;

import pt.pa.graph.Edge;
import pt.pa.graph.InvalidEdgeException;
import pt.pa.graph.Vertex;
import pt.pa.model.*;
import pt.pa.view.LogisticsNetworkUI;

import java.util.ArrayList;

/**
 * This class represents the Controller in the MVC pattern implementation.
 * Interacts both with the view and the model.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class LogisticsNetworkController {
    private final LogisticsNetworkUI view;
    private final LogisticsNetwork model;

    private final ArrayList<Hub> hubs;

    private final Caretaker caretaker;

    /**
     * Constructor. Initializes the controller.
     *
     * @param view  view of the MVC pattern
     * @param model model of the MVC pattern
     */
    public LogisticsNetworkController(LogisticsNetworkUI view, LogisticsNetwork model) {
        this.view = view;
        this.model = model;

        this.view.setTriggers(this);
        this.model.addObserver(this.view);

        this.hubs = new ArrayList<>();
        this.caretaker = new Caretaker(this.model);

    }

    /**
     * Makes a global import (importing the data into the program).
     */
    public void doLoadGlobal() {
        try {
            this.model.globalDataLoad(this.model.getGraph());
            this.view.resetGraphPanel();
            this.view.disableGlobalButton();

        } catch (LogisticsNetworkException e) {
            e.getMessage();
        }

    }

    /**
     * Exports the graph's data into a new folder.
     */
    public void doExportGlobal() {
        try {
            this.model.globalDataExport(this.model.getGraph());
        } catch (LogisticsNetworkException e) {
            e.getMessage();
        }
    }

    /**
     * Get the minimum cost path between two Hubs IDs.
     *
     * @param firstId  the ID of the first hub (origin).
     * @param secondId the ID of the second hub (destination).
     * @return int minimum cost between two hubs.
     */
    public int minCost(int firstId, int secondId) {
        return model.minimumCostPath(firstId, secondId, hubs);
    }

    public int longestMinCostPath() {
        return model.longestMinCostPath(hubs);
    }

    /**
     * Get the edges path of minimum cost path.
     *
     * @return ArrayList of edges, which are part of the path.
     */
    public ArrayList<Edge<Route, Hub>> getEdgesPath() {
        ArrayList<Edge<Route, Hub>> relations = new ArrayList<>();
        for (int i = 0; i < hubs.size() - 1; i++) {
            Vertex<Hub> auxHub = model.returnVertex(hubs.get(i));
            Vertex<Hub> auxHub1 = model.returnVertex(hubs.get(i + 1));

            for (Edge<Route, Hub> edges : model.getGraph().incidentEdges(auxHub)) {
                if (model.getGraph().opposite(auxHub, edges).equals(auxHub1)) {
                    relations.add(edges);
                }
            }
        }
        return relations;
    }

    /**
     * Adds a new route with a certain distance.
     */
    public void doAddRoute() {
        try {
            caretaker.saveState();

            String description = view.getRouteDistance();
            String id1 = view.getFirstHubId();
            String id2 = view.getSecondHubId();

            if (id1 == null || id2 == null) {
                view.displayError("You must select the IDs.");
                return;
            }

            if (description.trim().isEmpty()) {
                view.displayError("You must provide a distance.");
                return;
            }

            model.addRoute(description, Integer.parseInt(id1), Integer.parseInt(id2));

            view.clearError();
            view.clearControls();

        } catch (LogisticsNetworkException e) {
            this.view.displayError(e.getMessage());
        } catch (NumberFormatException e2) {
            view.displayError("The ID must be an integer number.");
        } catch (InvalidEdgeException e3) {
            view.displayError("There cannot be two Routes for the same pair of hubs!");
        }
    }

    /**
     * Removes a certain route, along with its distance.
     */
    public void doRemoveRoute() {
        try {
            caretaker.saveState();

            String id1 = view.getFirstHubId();
            String id2 = view.getSecondHubId();

            model.removeRoute(Integer.parseInt(id1), Integer.parseInt(id2));

            view.clearError();
            view.clearControls();

        } catch (LogisticsNetworkException e) {
            this.view.displayError(e.getMessage());
        } catch (NumberFormatException e2) {
            view.displayError("The ID must be an integer number.");
        }
    }

    /**
     * Returns a list of hubs.
     *
     * @return list of hubs
     */
    public ArrayList<Hub> getHubs() {
        return this.hubs;
    }

    /**
     * Returns the node of an hub.
     *
     * @param hub Hub
     * @return vertex which contains the hub
     */
    public Vertex<Hub> getHubNode(Hub hub) {
        return this.model.returnVertex(hub);
    }

    /**
     * Makes an undo in the application.
     *
     * @throws NoMementoException If undo fails
     */
    public void undo() throws NoMementoException {
        caretaker.restoreState();
        view.clearError();
        view.clearControls();
    }
}
