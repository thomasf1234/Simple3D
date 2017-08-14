package experiments.directorytree.factories;

import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.io.File;
import java.nio.file.Path;

//http://www.drdobbs.com/jvm/a-javafx-text-editor-part-1/240142297

/**
 * Created by tfisher on 07/08/2017.
 */
public class FileSystemTreeViewFactory {
    public static void build(FileSystemTreeView fileSystemTreeView, File directory) {
        fileSystemTreeView.clear();
        TreeItem<Path> root = fileSystemTreeView.getNodesForDirectory(directory);
        fileSystemTreeView.setRoot(root);

        //Set the factory for our TreeView
        fileSystemTreeView.setCellFactory(tv -> {
            TreeCell<Path> cell = new TreeCell<Path>() {
                @Override
                public void updateItem(Path item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setTooltip(null);
                        setContextMenu(null);
                    } else {
                        String fileName = item.getFileName().toString();
                        setText(fileName);
                        setTooltip(createTooltip(this));
                        setContextMenu(createContextMenu(this));
                    }
                }
            };

            return cell;
        });
    }

    //assumes build has been called
    public static void refresh(FileSystemTreeView fileSystemTreeView) {
        if (fileSystemTreeView.getRoot() != null) {
            TreeItem<Path> root = fileSystemTreeView.getRoot();
            File rootFile = root.getValue().toFile();

            if (rootFile.exists()) {
                if (rootFile.isDirectory()) {
                    TreeItem<Path> newRootTreeItem = fileSystemTreeView.getNodesForDirectory(rootFile);
                    fileSystemTreeView.setRoot(newRootTreeItem);
                }
            } else {
                fileSystemTreeView.setRoot(null);
            }
        }
    }

    //TODO : refresh filesystem on action, and raise alert on ContextMenu click if no longer valid
    protected static ContextMenu createContextMenu(TreeCell<Path> cell) {
        ContextMenu contextMenu = new ContextMenu();
        TreeItem<Path> treeItem = cell.getTreeItem();
        String fileName = treeItem.getValue().getFileName().toString();
        //Sub-Menu
        Menu newMenu = new Menu("New");
        MenuItem newMenuFileMenuItem = new MenuItem("File");
        MenuItem newMenuDirectoryMenuItem = new MenuItem("Directory");
        newMenu.getItems().addAll(newMenuFileMenuItem, newMenuDirectoryMenuItem);

        MenuItem refreshMenuItem = new MenuItem("Refresh");
        refreshMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileSystemTreeView fileSystemTreeView = (FileSystemTreeView) cell.getTreeView();
                refresh(fileSystemTreeView);
            }
        });

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File treeItemFile = treeItem.getValue().toFile();

                if (treeItemFile.exists()) {
                    boolean deleted = treeItemFile.delete();

                    if (deleted == true) {
                        FileSystemTreeView fileSystemTreeView = (FileSystemTreeView) cell.getTreeView();
                        refresh(fileSystemTreeView);
                    } else {
                        throw new RuntimeException("Could not delete file");
                    }
                }
            }
        });

        contextMenu.setAutoHide(true);
        contextMenu.getItems().setAll(newMenu, refreshMenuItem, deleteMenuItem);

        return contextMenu;
    }


    protected static Tooltip createTooltip(TreeCell<Path> cell) {
        Tooltip tooltip = new Tooltip();
        TreeItem<Path> treeItem = cell.getTreeItem();
        String absolutePath = treeItem.getValue().toAbsolutePath().toString();
        tooltip.setText(absolutePath);

        return tooltip;
    }
}

