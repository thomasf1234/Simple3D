package experiments.demofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;

public class SubController {

    @FXML public Label myLabel;
    @FXML public Group root;
    @FXML public Group editGroup;
    @FXML public Group nonEditGroup;

    public void initialize() {
        myLabel.setText("blobby");
    }

    public void openTo(ActionEvent actionEvent) {
    }
}
