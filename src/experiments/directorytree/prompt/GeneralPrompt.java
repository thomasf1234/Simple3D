package experiments.directorytree.prompt;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

//http://code.makery.ch/blog/javafx-dialogs-official/
public class GeneralPrompt {
    public static boolean showAndGetConfirmation(String message) {
        return showAndGetConfirmation("", message);
    }

    public static boolean showAndGetConfirmation(String headerText, String message) {
        boolean confirmed = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            confirmed = true;
            // ... user chose OK
        } else {
            // ... user chose CANCEL or closed the dialog
        }

        return confirmed;
    }
}
