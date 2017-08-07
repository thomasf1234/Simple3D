package experiments.directorytree.factories;

import experiments.directorytree.ContextMenuTreeView;
import experiments.directorytree.Util;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by tfisher on 07/08/2017.
 */
public class FileSystemTreeViewFactory {
    public static void build(ContextMenuTreeView<Path> contextMenuTreeView, File directory) {
        TreeItem<Path> root = Util.getNodesForDirectory(directory);

        //Set the factory for our TreeView
        contextMenuTreeView.setCellFactory(tv ->  {
            final Tooltip tooltip = new Tooltip();
            TreeCell<Path> cell = new TreeCell<Path>() {
                @Override
                public void updateItem(Path item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        String fileName = item.getFileName().toString();
                        setText(fileName);
                        try {
                            tooltip.setText(item.toRealPath().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setTooltip(tooltip);
                    }
                }
            };

            return cell ;
        });

        // Define the mouse event handler for the TreeView
        EventHandler mouseEventHandler = new EventHandler<MouseEvent>() {
            public void handle (MouseEvent mouseEvent){
                //by default we will ensure that the contextMenu will be hidden if there is one showing
                boolean hideContextMenu = true;
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        //Retrieve the node we have clicked on
                        Node node = mouseEvent.getPickResult().getIntersectedNode();

                        TreeCell treeCell = null;
                        //Check if we've clicked our TreeCell or child (TODO: recursvive child check)
                        if (node instanceof TreeCell) {
                            treeCell = (TreeCell) node;
                        } else if (node.getParent() != null && node.getParent() instanceof TreeCell) {
                            treeCell = (TreeCell) node.getParent();
                        }

                        //treeCell will not have been set if empty space
                        if (treeCell != null) {
                            //only interested in our TreeCell<Path> instances
                            if (treeCell.getItem() instanceof Path) {
                                TreeCell<Path> pathTreeCell = (TreeCell<Path>) treeCell;

                                Path path = pathTreeCell.getItem();

                                if (path != null) {
                                    contextMenuTreeView.ensureHideContextMenu();
                                    String fileName = path.getFileName().toString();
                                    ContextMenu contextMenu = getRightClickContextMenu(fileName);
                                    contextMenuTreeView.setContextMenu2(contextMenu);
                                    contextMenu.show(contextMenuTreeView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                                    hideContextMenu = false;
                                }
                            }
                        }
                    }
                }

                if (hideContextMenu == true) {
                    contextMenuTreeView.ensureHideContextMenu();
                }

                mouseEvent.consume();
            }
        };

        contextMenuTreeView.addEventHandler(MouseEvent.ANY, mouseEventHandler);
        contextMenuTreeView.setRoot(root);
    }

    protected static ContextMenu getRightClickContextMenu(String s) {
        ContextMenu contextMenu = new ContextMenu();
        //Sub-Menu
        Menu addMenu = new Menu(String.format("New %s", s));
        MenuItem addModelMenuItem = new MenuItem("Model");
        MenuItem addSceneMenuItem = new MenuItem("Scene");
        addMenu.getItems().addAll(addModelMenuItem, addSceneMenuItem);
        contextMenu.getItems().addAll(addMenu);
        contextMenu.setAutoHide(true);
        return contextMenu;
    }
}
