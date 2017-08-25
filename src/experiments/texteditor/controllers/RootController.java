package experiments.texteditor.controllers;

import experiments.directorytree.controllers.Controller;
import experiments.directorytree.prompt.FilePrompt;
import experiments.directorytree.utils.FileUtils;
import experiments.texteditor.CodeArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;

public class RootController extends Controller {
    @FXML public CodeArea codeArea;

    public void openFile(ActionEvent actionEvent) {
        File file = FilePrompt.openFile(getStage());

        if (file != null) {
            try {
                codeArea.openFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsFile(ActionEvent actionEvent) {
        try {
            saveAs();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred saving file");
            alert.setContentText(String.format("%s", e.getMessage()));
            alert.showAndWait();
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        if (codeArea.hasOpenFile()) {
            try {
                FileUtils.write(codeArea.getOpenFile(), codeArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error occurred saving file");
                alert.setContentText(String.format("%s", e.getMessage()));
                alert.showAndWait();
            }
        } else {
            try {
                saveAs();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error occurred saving file");
                alert.setContentText(String.format("%s", e.getMessage()));
                alert.showAndWait();
            }
        }
    }

    protected void saveAs() throws IOException {
        File savedFile = FilePrompt.saveFile(getStage(), codeArea.getText());

        if (savedFile != null) {
            codeArea.openFile(savedFile);
        }
    }

    public void undo(ActionEvent actionEvent) {
        codeArea.undo();
    }
}
