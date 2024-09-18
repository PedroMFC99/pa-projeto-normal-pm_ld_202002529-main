import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.pa.controller.LogisticsNetworkController;
import pt.pa.model.LogisticsNetwork;
import pt.pa.view.LogisticsNetworkView;

import java.io.FileNotFoundException;

/**
 * Represents the Client - initializes the Application.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws NumberFormatException {
        LogisticsNetwork model = new LogisticsNetwork();
        LogisticsNetworkView view = new LogisticsNetworkView(model);
        LogisticsNetworkController controller = new LogisticsNetworkController(view, model);

        Scene scene = new Scene(view, 1400, 968);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Logistics Network");
        stage.setMinHeight(768);
        stage.setMinWidth(1200);
        stage.setScene(scene);
        stage.show();

        view.initGraphDisplay();
    }
}


