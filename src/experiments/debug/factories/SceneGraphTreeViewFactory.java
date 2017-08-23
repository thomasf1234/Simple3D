package experiments.debug.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class SceneGraphTreeViewFactory {
    public static void build(TreeView<Node> treeView, TextArea outTextArea, Node root) {
        TreeItem<Node> rootTreeItem = getNodesTreeItem(root);
        treeView.setRoot(rootTreeItem);


        //Set the factory for our TreeView
        treeView.setCellFactory(tv -> {
            TreeCell<Node> cell = new TreeCell<Node>() {
                @Override
                public void updateItem(Node item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setTooltip(null);
                        setContextMenu(null);
                        setGraphic(null);

                    } else {

                        setText(createText(this));
                        setTooltip(createTooltip(this));
                        setContextMenu(createContextMenu(this, outTextArea));
                    }
                }
            };

            return cell;
        });
    }

    protected static TreeItem<Node> getNodesTreeItem(Node node) {
        TreeItem<Node> nodeTreeItem = new TreeItem<Node>(node);

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                nodeTreeItem.getChildren().add(getNodesTreeItem(child));
            }
        }

        return nodeTreeItem;
    }

    protected static String createText(TreeCell<Node> cell) {
        TreeItem<Node> treeItem = cell.getTreeItem();
        Node node = treeItem.getValue();
        String fullClassName = node.getClass().getName();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

        return simpleClassName;
    }

    protected static Tooltip createTooltip(TreeCell<Node> cell) {
        TreeItem<Node> treeItem = cell.getTreeItem();
        Node node = treeItem.getValue();
        String fullClassName = node.getClass().getName();
        Tooltip tooltip = new Tooltip();
        tooltip.setText(fullClassName);

        return tooltip;
    }

    protected static ContextMenu createContextMenu(TreeCell<Node> cell, TextArea outTextArea) {
        TreeItem<Node> treeItem = cell.getTreeItem();
        Node node = treeItem.getValue();
        String debugInfo = node.toString();
        //Sub-Menu
        MenuItem dumpMenuItem = new MenuItem("Dump");
        dumpMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                outTextArea.setText(debugInfo);
            }
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);
        contextMenu.getItems().setAll(dumpMenuItem);

        return contextMenu;
    }
}
