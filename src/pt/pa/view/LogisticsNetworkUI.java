package pt.pa.view;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import pt.pa.controller.LogisticsNetworkController;
import pt.pa.observerpattern.Observer;

/**
 * LogisticsNetwork (view) - Interface.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public interface LogisticsNetworkUI extends Observer {
    void displayError(String msg);

    void clearError();

    void clearControls();

    Alert createAlert(String title, String headerText, Alert.AlertType alertType);

    void disableGlobalButton();

    void enableGlobalButton();

    TextInputDialog createDialog(String defaultValue, String headerText);

    void displayMinPath(LogisticsNetworkController controller, String ids);

    String getFirstHubId();

    String getSecondHubId();

    String getRouteDistance();

    void setLblGetCentralizedHubs(String text);

    void resetGraphPanel();

    void setTriggers(LogisticsNetworkController controller);
}

