package pt.pa.view;

import com.brunomnsilva.smartgraph.graphview.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.pa.controller.LogisticsNetworkController;
import pt.pa.graph.Edge;
import pt.pa.graph.Vertex;
import pt.pa.model.Hub;
import pt.pa.model.LogisticsNetwork;
import pt.pa.model.Route;
import pt.pa.observerpattern.Observable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents the 'View' component in the MVC pattern.
 * It is essentially the front-end of the application.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class LogisticsNetworkView extends BorderPane implements LogisticsNetworkUI {
    private final LogisticsNetwork model;
    private SmartGraphPanel<Hub, Route> graphPanel;

    private Button btGlobalImport;
    private Button btGlobalExport;
    private Button btShortestPath;
    private Button btLongestShortestPath;

    private Label lblGetCentralizedHubs;

    private Label lblNumHubs;
    private Label lblNumRoutes;
    private Label lblNumsubGraphs;

    private ComboBox<String> cbHubId1;
    private ComboBox<String> cbHubId2;
    private TextField txtDistance;
    private Button btAddDistance;
    private Button btRemoveDistance;
    private Button btGetCentralizedHubs;
    private Button btGetTop5CentralizedHubs;
    private Button btUndo;


    public LogisticsNetworkView(LogisticsNetwork model) {
        this.model = model;

        createLayout();
    }

    /**
     * Updates the object being observed.
     *
     * @param subject Observable
     * @param arg     Object
     */
    @Override
    public void update(Observable subject, Object arg) {
        if (subject == model) {
            graphPanel.updateAndWait();

            lblNumHubs.setText(model.getNumberOfHubs() + "");
            lblNumRoutes.setText(model.getNumberOfRoutes() + "");
            lblGetCentralizedHubs.setText(model.getCentralizedHubsText() + "");
            this.lblNumsubGraphs.setText(String.valueOf(model.getSubGrapthCount()));

            if (this.model.getGraph() == null) {
                this.btGetTop5CentralizedHubs.setDisable(true);
                this.btGetCentralizedHubs.setDisable(true);
                this.lblGetCentralizedHubs.setDisable(true);
                this.btShortestPath.setDisable(true);
            }

            cbHubId1.getItems().clear();
            cbHubId2.getItems().clear();
            List<Hub> hubList = new ArrayList<>(model.getHubs());
            hubList.sort(Comparator.comparingInt(Hub::getIdentifier));
            for (Hub p : hubList) {
                cbHubId1.getItems().add(String.valueOf(p.getIdentifier()));
                cbHubId2.getItems().add(String.valueOf(p.getIdentifier()));
            }
        }
    }

    /**
     * Initializes the app's layout.
     */
    private void createLayout() {
        HBox hbTop = new HBox(10);
        Background background = new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(1), null));
        hbTop.setBackground(background);
        hbTop.setPadding(new Insets(0, 4, 0, 2));
        setTop(hbTop);

        VBox vSidePanel = new VBox(10);
        vSidePanel.setBackground(background);
        vSidePanel.setPadding(new Insets(0, 4, 0, 2));
        setRight(vSidePanel);

        SmartGraphProperties smartGraphProperties = new SmartGraphProperties("edge.label = true" + "\n" + "edge.arrow = false");
        graphPanel = new SmartGraphPanel<>(model.getGraph(), smartGraphProperties, new SmartCircularSortedPlacementStrategy());
        setCenter(graphPanel);

        this.btGlobalImport = new Button("Global Import");
        this.btGlobalExport = new Button("Global Export");
        this.btShortestPath = new Button("Calculate shortest path");
        this.btLongestShortestPath = new Button("Longest short path");
        this.btGetCentralizedHubs = new Button("Centralized Hubs");
        this.btGetTop5CentralizedHubs = new Button("Top 5 Most Central Hubs");

        Label lblNumHubsDescription = new Label("Number of Hubs:");
        lblNumHubs = new Label();
        lblNumHubs.setStyle("-fx-font-weight: bold;");

        Label lblNumRoutesDescription = new Label("Number of Routes:");
        lblNumRoutes = new Label();
        lblNumRoutes.setStyle("-fx-font-weight: bold;");

        Label lblNumsubGraphsDescription = new Label("Number of Sub-Graphs:");
        lblNumsubGraphs = new Label();
        lblNumsubGraphs.setStyle("-fx-font-weight: bold;");

        GridPane relationPane = new GridPane();
        relationPane.setBackground(background);

        relationPane.setAlignment(Pos.CENTER);
        relationPane.setHgap(5);
        relationPane.setVgap(5);
        relationPane.setPadding(new Insets(10, 10, 10, 10)); // set top, right, bottom, left

        txtDistance = new TextField("");
        cbHubId1 = new ComboBox<>();
        cbHubId1.setMaxWidth(Double.MAX_VALUE); //hack to hgrow
        cbHubId2 = new ComboBox<>();
        cbHubId2.setMaxWidth(Double.MAX_VALUE); //hack to hgrow

        Label labelId1 = new Label("Id 1: ");
        Label labelId2 = new Label("Id 2: ");
        Label labelDesc = new Label("Dist: ");

        relationPane.add(labelId1, 0, 1);
        relationPane.add(cbHubId1, 1, 1);
        relationPane.add(labelId2, 0, 2);
        relationPane.add(cbHubId2, 1, 2);
        relationPane.add(labelDesc, 0, 3);
        relationPane.add(txtDistance, 1, 3);

        btAddDistance = new Button("Add");
        HBox addRel = new HBox(btAddDistance);
        btRemoveDistance = new Button("Remove");
        btRemoveDistance.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btUndo = new Button("Undo");

        relationPane.add(addRel, 1, 4);
        relationPane.add(btRemoveDistance, 1, 5);
        relationPane.add(btUndo, 1, 6);

        lblGetCentralizedHubs = new Label();

        vSidePanel.getChildren().addAll(relationPane, new Separator());
        vSidePanel.setAlignment(Pos.TOP_CENTER);

        this.btGlobalExport.setDisable(true);
        this.btShortestPath.setDisable(true);
        this.btGetCentralizedHubs.setDisable(true);
        this.btGetTop5CentralizedHubs.setDisable(true);
        this.lblGetCentralizedHubs.setDisable(true);
        this.btLongestShortestPath.setDisable(true);
        hbTop.getChildren().addAll(btGlobalImport, btGlobalExport, lblNumHubsDescription, lblNumHubs, lblNumRoutesDescription, lblNumRoutes, lblNumsubGraphsDescription, lblNumsubGraphs, btShortestPath, btLongestShortestPath, btGetCentralizedHubs, btGetTop5CentralizedHubs);
        hbTop.setAlignment(Pos.CENTER);

        HBox bottom = new HBox(10);
        CheckBox automatic = new CheckBox("Automatic layout");
        automatic.selectedProperty().bindBidirectional(graphPanel.automaticLayoutProperty());
        bottom.getChildren().add(automatic);
        setBottom(bottom);
    }

    /**
     * Sets the different actions of the buttons.
     *
     * @param controller Controller
     */
    @Override
    public void setTriggers(LogisticsNetworkController controller) {
        this.btGlobalImport.setOnAction(e -> {
            this.btGlobalExport.setDisable(false);
            this.btGetTop5CentralizedHubs.setDisable(false);
            this.btGetCentralizedHubs.setDisable(false);
            this.lblGetCentralizedHubs.setDisable(false);
            this.btShortestPath.setDisable(false);
            this.btLongestShortestPath.setDisable(false);
            controller.doLoadGlobal();
            this.lblNumsubGraphs.setText(String.valueOf(model.getSubGrapthCount()));
        });
        this.btGlobalExport.setOnAction(e -> controller.doExportGlobal());
        this.btShortestPath.setOnAction(e -> {
            TextInputDialog td = createDialog(
                    "1-2",
                    "Enter Hub ID#1 and Hub ID#2 in the following format (id1-id2)"
            );
            td.show();
            td.setOnCloseRequest(e1 -> displayMinPath(controller, td.getResult()));
        });
        this.btLongestShortestPath.setOnAction(e -> displayMostDistantPairOfHubs(controller));
        this.btGetCentralizedHubs.setOnAction(event -> {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(lblGetCentralizedHubs);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(stackPane);

            update(model, null);

            Scene scene = new Scene(scrollPane, 500, 500);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        });
        this.btGetTop5CentralizedHubs.setOnAction(event -> {
            StackPane stackPane = new StackPane();

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(stackPane);

            List<String> info = model.getTop5CentralizedHubsList();

            Alert a = createAlert(
                    "Top 5 Most Central Hubs",
                    "Top 5 Most Central Hubs",
                    Alert.AlertType.INFORMATION
            );


            final BarChart<String, Number> bc = createBarChart(
                    "Top 5 Most Central Hubs",
                    "Hubs",
                    "Nº Adjacent Hubs",
                    "Number of Adjacent Hubs",
                    info
            );
            a.getDialogPane().setContent(bc);
            a.setResizable(true);

            a.show();

            update(model, null);
        });

        this.btAddDistance.setOnAction(event -> controller.doAddRoute());

        this.btRemoveDistance.setOnAction(event -> {
            Alert alert = makeConfirmationDialog("Delete Route", "Are you sure?");
            alert.showAndWait().ifPresent(response -> {
                if (response.getButtonData() == ButtonBar.ButtonData.YES) {
                    controller.doRemoveRoute();
                }

            });
        });
        this.btUndo.setOnAction(event -> {
            controller.undo();
            update(model, null);
            resetGraphPanel();
        });
    }

    /**
     * Creates a bar chart.
     *
     * @param title      String
     * @param labelX     String
     * @param labelY     String
     * @param seriesName String
     * @param data       List<String>
     * @return the created bar chart.
     */
    public BarChart<String, Number> createBarChart(String title, String labelX, String labelY, String seriesName, List<String> data) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final int columnsSize = 5;

        final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle(title);
        xAxis.setLabel(labelX);
        yAxis.setLabel(labelY);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName(seriesName);

        for (int i = 0; i < columnsSize; i++) {
            series1.getData().add(new XYChart.Data(data.get(i + columnsSize), Integer.parseInt(data.get(i))));
        }

        bc.getData().addAll(series1);
        return bc;
    }

    /**
     * Creates an alert of the 'Confirmation Type'.
     *
     * @param title String
     * @param text  String
     * @return the created alert
     */
    private Alert makeConfirmationDialog(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);
        return alert;
    }

    /**
     * Sets the text of the label.
     *
     * @param text String
     */
    @Override
    public void setLblGetCentralizedHubs(String text) {
        this.lblGetCentralizedHubs.setText(text);
    }

    /**
     * Display an error message.
     *
     * @param msg String
     */
    @Override
    public void displayError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.show();
    }

    /**
     * Clears an error message.
     */
    @Override
    public void clearError() {
    }

    /**
     * Clears the 'txtdistance' textfield.
     */
    @Override
    public void clearControls() {
        txtDistance.clear();
    }

    /**
     * Creates a default alert.
     *
     * @param title      String
     * @param headerText headerText
     * @param alertType  Alert.AlertType
     * @return the newly created alert.
     */
    @Override
    public Alert createAlert(String title, String headerText, Alert.AlertType alertType) {
        Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(headerText);
        return a;
    }

    /**
     * Disables the GlobalImport button.
     */
    @Override
    public void disableGlobalButton() {
        this.btGlobalImport.setDisable(true);
    }

    /**
     * Enables the GlobaLImport button
     */
    @Override
    public void enableGlobalButton() {
        this.btGlobalImport.setDisable(false);
    }

    /**
     * Creates a textInputDialog box.
     *
     * @param defaultValue String
     * @param headerText   String
     * @return the newly created textInputDialog
     */
    @Override
    public TextInputDialog createDialog(String defaultValue, String headerText) {
        TextInputDialog td = new TextInputDialog(defaultValue);
        td.setHeaderText(headerText);
        return td;
    }

    /**
     * Displays the shortest path between two Hubs, using their IDs.
     *
     * @param controller LogisticsNetworkController.
     * @param ids        String with the two hubs IDs.
     */
    @Override
    public void displayMinPath(LogisticsNetworkController controller, String ids) {
        ArrayList<Integer> hubsId = splitIds(ids);

        if (hubsId == null) return;

        int firstHub = hubsId.get(0);
        int secondHub = hubsId.get(1);
        int minCost = controller.minCost(firstHub, secondHub);

        if (minCost == -1) {
            createAlert(
                    "Minimum Cost Path",
                    "The minimum Cost Path between " + firstHub + " and " + secondHub + " could not be calculated!",
                    Alert.AlertType.ERROR
            ).show();
        } else {
            StringBuilder info = new StringBuilder("IDs of the path: ");
            int count = 0;
            Alert a = createAlert(
                    "Minimum Cost Path",
                    "Calculating the minimum cost path...",
                    Alert.AlertType.INFORMATION
            );
            for (Hub u : controller.getHubs()) {
                info.append(u).append(count > 0 ? " " : "-");
                count++;
            }
            TextArea area = createTextArea(
                    "The minimum cost between " + "id " + firstHub
                            + " and " + "id " + secondHub + " is " + minCost + " kms " + "\n" + info,
                    true,
                    false
            );
            a.getDialogPane().setContent(area);
            a.setResizable(true);
            a.show();
            drawPath(controller);
        }
    }

    /**
     * Displays the most distant pair of hubs.
     *
     * @param controller LogisticsNetworkController
     */
    public void displayMostDistantPairOfHubs(LogisticsNetworkController controller) {
        int longestMinCost = controller.longestMinCostPath();

        if (longestMinCost == -1) {
            createAlert(
                    "Minimum Cost Path",
                    "The minimum Cost Path between the most distant hubs could not be calculated!",
                    Alert.AlertType.ERROR
            ).show();
        } else {
            StringBuilder info = new StringBuilder("IDs of the path: ");
            int count = 0;
            Alert a = createAlert(
                    "Minimum Cost Path between most distant hubs",
                    "Calculating the minimum cost path between most distant hubs...",
                    Alert.AlertType.INFORMATION
            );
            for (Hub u : controller.getHubs()) {
                info.append(u).append(count > 0 ? " " : "-");
                count++;
            }
            TextArea area = createTextArea(
                    "The minimum cost between the most distant hubs is " + longestMinCost + " kms " + "\n" + info,
                    true,
                    false
            );
            a.getDialogPane().setContent(area);
            a.setResizable(true);
            a.show();
            drawPath(controller);
        }
    }

    /**
     * Splits two IDs, for the minimum-cost method.
     *
     * @param idString String with two IDs.
     * @return ArrayList with the String's ID, or Null.
     */
    private ArrayList<Integer> splitIds(String idString) {
        ArrayList<Integer> splitIds = new ArrayList<>();
        try {
            String[] inputMinCost = idString.split("-");
            splitIds.add(Integer.parseInt(inputMinCost[0]));
            splitIds.add(Integer.parseInt(inputMinCost[1]));
            return splitIds;
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException except) {
            createAlert(
                    "Minimum Cost Path",
                    "Please, insert a valid format.\nExample: 1-99",
                    Alert.AlertType.ERROR
            ).show();
        }
        return null;
    }

    public TextArea createTextArea(String text, boolean wrapText, boolean editable) {
        TextArea area = new TextArea(text);
        area.setWrapText(wrapText);
        area.setEditable(editable);
        return area;
    }

    /**
     * Draws a colored path (shortest path between two Hubs) on the Graph.
     *
     * @param controller LogisticsNetworksController
     */
    private void drawPath(LogisticsNetworkController controller) {
        for (Edge<Route, Hub> edgeRoute : controller.getEdgesPath()) {
            graphPanel.getStylableEdge(edgeRoute).addStyleClass("edge-cost");
            graphPanel.getStylableEdge(edgeRoute).setStyle("-fx-stroke: green; -fx-stroke-width: 3;");
        }
        for (Hub u : controller.getHubs()) {
            Vertex<Hub> vertexHub = controller.getHubNode(u);
            graphPanel.getStylableVertex(vertexHub).addStyleClass("vertex-cost");
            graphPanel.getStylableVertex(vertexHub).setStyle("-fx-stroke: purple; -fx-stroke-width: 3;");
        }
    }

    /**
     * Returns the first hub id.
     *
     * @return the first hub id.
     */
    public String getFirstHubId() {
        return cbHubId1.getSelectionModel().getSelectedItem();
    }

    /**
     * Returns the second hub id.
     *
     * @return second hub id.
     */
    public String getSecondHubId() {
        return cbHubId2.getSelectionModel().getSelectedItem();
    }

    /**
     * Returns the route distance.
     *
     * @return the route distance.
     */
    public String getRouteDistance() {
        return txtDistance.getText();
    }

    /**
     * Resets the graphPanel.
     */
    @Override
    public void resetGraphPanel() {
        for (Vertex v : this.model.getGraph().vertices()) {
            Hub temp = (Hub) v.element();
            graphPanel.setVertexPosition(v, temp.getCoordinates().getX(), temp.getCoordinates().getY());
        }

    }

    /**
     * Initializes the display of the Graph.
     */
    public void initGraphDisplay() {
        this.graphPanel.init();
        update(model, null);
    }

}
