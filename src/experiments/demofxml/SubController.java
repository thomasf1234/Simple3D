package experiments.demofxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import simple3d.Director;

public class SubController {

    @FXML public Label myLabel;
    @FXML public CheckBox clickMeCheckBox;
    @FXML public Pane pane;
//    @FXML public Group root;
//    @FXML public Group editGroup;
//    @FXML public Group nonEditGroup;
    @FXML public SubScene subScene;
    private String someString;

    public void initialize() {
//        myLabel.setText("blobby");
        Director director = new Director();
        pane.getChildren().add(director.getSubScene());
    }

    public void setString(String s) {
        this.someString = s;
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
        System.out.println(String.format("someString: %s", someString));
    }
}
