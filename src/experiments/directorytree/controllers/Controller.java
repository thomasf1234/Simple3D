package experiments.directorytree.controllers;

import experiments.directorytree.singletons.SLogger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Stage;

//root node in fxml must have id: root
public abstract class Controller {
    @FXML protected Parent root;

    public void initialize() {
        String logMessage = String.format("%s initializing", getClassName());
        SLogger.getInstance().log(logMessage);
    }

    protected String getClassName() {
        return this.getClass().getName();
    }

    protected Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }
}
