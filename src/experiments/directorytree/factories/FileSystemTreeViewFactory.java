package experiments.directorytree.factories;

import experiments.directorytree.Main;
import experiments.directorytree.Util;
import experiments.directorytree.prompt.FilePrompt;
import experiments.directorytree.singletons.SImages;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

//http://www.drdobbs.com/jvm/a-javafx-text-editor-part-1/240142297

/**
 * Created by tfisher on 07/08/2017.
 */
public class FileSystemTreeViewFactory {
    public static void build(FileSystemTreeView fileSystemTreeView, File directory) {
        fileSystemTreeView.clear();
        TreeItem<File> root = fileSystemTreeView.getNodesForDirectory(directory);
        fileSystemTreeView.setRoot(root);

        //Set the factory for our TreeView
        fileSystemTreeView.setCellFactory(tv -> {
            TreeCell<File> cell = new TreeCell<File>() {
                @Override
                public void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setTooltip(null);
                        setContextMenu(null);
                        setGraphic(null);
                    } else {
                        setText(createText(this));
                        setTooltip(createTooltip(this));
                        setContextMenu(createContextMenu(this));
                        setGraphic(createGraphic(this));
                    }
                }
            };

            return cell;
        });
    }

    //assumes build has been called
    public static void refresh(FileSystemTreeView fileSystemTreeView) {
        if (fileSystemTreeView.getRoot() != null) {
            TreeItem<File> root = fileSystemTreeView.getRoot();
            File rootFile = root.getValue();

            if (rootFile.exists()) {
                if (rootFile.isDirectory()) {
                    TreeItem<File> newRootTreeItem = fileSystemTreeView.getNodesForDirectory(rootFile);
                    fileSystemTreeView.setRoot(newRootTreeItem);
                }
            } else {
                fileSystemTreeView.setRoot(null);
            }
        }
    }

    protected static ContextMenu createContextMenu(TreeCell<File> cell) {
        ContextMenu contextMenu = new ContextMenu();
        TreeItem<File> treeItem = cell.getTreeItem();
        File treeItemFile = treeItem.getValue();
        String fileName = treeItemFile.getName();
        //Sub-Menu
        Menu newMenu = new Menu("New");
        MenuItem newMenuFileMenuItem = new MenuItem("File");
        newMenuFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileSystemTreeView fileSystemTreeView = (FileSystemTreeView) cell.getTreeView();

                try {
                    FilePrompt.newFile(treeItemFile);
                    refresh(fileSystemTreeView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        MenuItem newMenuDirectoryMenuItem = new MenuItem("Directory");
        newMenuDirectoryMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileSystemTreeView fileSystemTreeView = (FileSystemTreeView) cell.getTreeView();

                try {
                    FilePrompt.newDirectory(treeItemFile);
                    refresh(fileSystemTreeView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
        contextMenu.getItems().setAll(newMenu, new SeparatorMenuItem(), refreshMenuItem, deleteMenuItem);

        return contextMenu;
    }


    protected static String createText(TreeCell<File> cell) {
        TreeItem<File> treeItem = cell.getTreeItem();
        File file = treeItem.getValue();
        String fileName = file.getName();

        return fileName;
    }

    protected static Tooltip createTooltip(TreeCell<File> cell) {
        Tooltip tooltip = new Tooltip();
        TreeItem<File> treeItem = cell.getTreeItem();
        File file = treeItem.getValue();
        String absolutePath = file.getAbsolutePath();
        tooltip.setText(absolutePath);

        return tooltip;
    }

    protected static ImageView createGraphic(TreeCell<File> cell) {
        ImageView imageView = new ImageView();
        TreeItem<File> treeItem = cell.getTreeItem();
        File file = treeItem.getValue();

        if (file.exists()) {
            if (file.isDirectory()) {
                imageView.setImage(SImages.getInstance().getImage("blue-folder.png"));
            } else {
                String extension = Util.getFileExtension(file);

                if (Objects.equals(extension, ".txt")) {

                } else {
                    imageView.setImage(SImages.getInstance().getImage("file.png"));
                }
            }
        }

        return imageView;
    }
}

