package experiments.directorytree.factories;

import experiments.directorytree.ContextMenuTreeView;
import experiments.directorytree.Util;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
        contextMenuTreeView.setCellFactory(tv -> {
            TreeCell<Path> cell = new TreeCell<Path>() {
                @Override
                public void updateItem(Path item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        getTooltip().setText(null);
                        getContextMenu().getItems().clear();
                    } else {
                        String fileName = item.getFileName().toString();
                        setText(fileName);
                        try {
                            updateToolTip(this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        updateContextMenu(this);
                    }
                }
            };

            //must set these
            cell.setTooltip(new Tooltip());
            cell.setContextMenu(new ContextMenu());

            return cell;
        });

        contextMenuTreeView.setRoot(root);


    }

    protected static void updateContextMenu(TreeCell<Path> cell) {
        ContextMenu contextMenu = cell.getContextMenu();
        TreeItem<Path> treeItem = cell.getTreeItem();
        String fileName = treeItem.getValue().getFileName().toString();
        //Sub-Menu
        MenuItem refreshMenuItem = new MenuItem("Refresh");
        refreshMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File treeItemFile = treeItem.getValue().toFile();
                treeItem.getChildren().clear();

                if (treeItemFile.exists()) {
                    ObservableList<TreeItem<Path>> children = Util.getNodesForDirectory(treeItemFile).getChildren();
                    treeItem.getChildren().addAll(children);
                } else {
                    TreeView<Path> treeView = cell.getTreeView();
                    if (treeItem == treeView.getRoot()) {
                        treeView.setRoot(null);
                    } else {
                        TreeItem<Path> treeItemParent = treeItem.getParent();
                        treeItemParent.getChildren().remove(treeItem);
                    }
                }
            }
        });

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File treeItemFile = treeItem.getValue().toFile();
                treeItem.getChildren().clear();

                if (treeItemFile.exists()) {
                    ObservableList<TreeItem<Path>> children = Util.getNodesForDirectory(treeItemFile).getChildren();
                    treeItem.getChildren().addAll(children);
                } else {
                    TreeView<Path> treeView = cell.getTreeView();
                    if (treeItem == treeView.getRoot()) {
                        treeView.setRoot(null);
                    } else {
                        TreeItem<Path> treeItemParent = treeItem.getParent();
                        treeItemParent.getChildren().remove(treeItem);
                    }
                }
            }
        });
        
        Menu addMenu = new Menu(String.format("New %s", fileName));
        MenuItem addModelMenuItem = new MenuItem("Model");
        MenuItem addSceneMenuItem = new MenuItem("Scene");
        addMenu.getItems().addAll(addModelMenuItem, addSceneMenuItem);
        contextMenu.getItems().clear();
        contextMenu.getItems().addAll(refreshMenuItem, addMenu);
        contextMenu.setAutoHide(true);
    }

    protected static void updateToolTip(TreeCell<Path> cell) throws IOException {
        Tooltip tooltip = cell.getTooltip();
        TreeItem<Path> treeItem = cell.getTreeItem();
        String realPath = treeItem.getValue().toRealPath().toString();
        tooltip.setText(realPath);
    }
}

