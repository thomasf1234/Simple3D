package experiments.demofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class SubController {

    @FXML public Label myLabel;
    @FXML public CheckBox clickMeCheckBox;
//    @FXML public Group root;
//    @FXML public Group editGroup;
//    @FXML public Group nonEditGroup;

    public void initialize() {
//        myLabel.setText("blobby");
    }

    public void openTo(ActionEvent actionEvent) {
        System.out.println(actionEvent.getEventType().toString());
    }

    public void showAboutDialog(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JavaFX About");
        alert.setHeaderText("experiments.demofxml v0.0.1");
        String s ="This is an example of JavaFX 8 Dialogs... ";
        alert.setContentText(s);
        alert.show();
    }

    public void clickMea(ActionEvent actionEvent) {
        System.out.println(String.format("checkbox clickme is selected: %s", clickMeCheckBox.isSelected()));
    }
}
