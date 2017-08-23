package experiments.debug.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;

public class RootController {
    @FXML protected Parent root;
    @FXML
    public TreeView<Node> nodeTreeView;
    @FXML
    public TextArea debugTextArea;

    public TextArea getDebugTextArea() {
        return debugTextArea;
    }

    public TreeView<Node> getNodeTreeView() {
        return nodeTreeView;
    }
}
