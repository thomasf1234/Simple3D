package experiments.directorytree.factories;

import experiments.directorytree.prompt.GeneralPrompt;
import experiments.directorytree.utils.FileUtils;
import experiments.directorytree.prompt.FilePrompt;
import experiments.directorytree.singletons.SImages;
import experiments.directorytree.tree_views.FileSystemTreeView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                        clearDragEvents(this);

                    } else {
                        setText(createText(this));
                        setTooltip(createTooltip(this));
                        setContextMenu(createContextMenu(this));
                        setGraphic(createGraphic(this));
                        setOnDragEvents(this);
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
                    boolean confirmation = GeneralPrompt.showAndGetConfirmation(String.format("Are you sure you want to delete %s?", fileName));

                    if (confirmation) {
                        boolean deleted = treeItemFile.delete();

                        if (deleted) {
                            FileSystemTreeView fileSystemTreeView = (FileSystemTreeView) cell.getTreeView();
                            refresh(fileSystemTreeView);
                        } else {
                            throw new RuntimeException("Could not delete file");
                        }
                    }
                }
            }
        });

        contextMenu.setAutoHide(true);

        if (treeItemFile.isDirectory()) {
            contextMenu.getItems().setAll(newMenu, new SeparatorMenuItem(), refreshMenuItem, deleteMenuItem);
        } else {
            contextMenu.getItems().setAll(deleteMenuItem);
        }

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
                String extension = FileUtils.getFileExtension(file);

                if (Objects.equals(extension, ".txt")) {
                    imageView.setImage(SImages.getInstance().getImage("file.png"));
                } else if (Objects.equals(extension, ".simple3d")) {
                    imageView.setImage(SImages.getInstance().getImage("3D_cube.png"));
                } else {
                    imageView.setImage(SImages.getInstance().getImage("file.png"));
                }
            }
        }

        return imageView;
    }

    protected static void setOnDragEvents(TreeCell<File> cell) {
        TreeItem<File> treeItem = cell.getTreeItem();
        File file = treeItem.getValue();

        cell.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");

                /* allow any transfer mode */
                Dragboard dragboard = cell.startDragAndDrop(TransferMode.COPY_OR_MOVE);

                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(file.getAbsolutePath());
                dragboard.setContent(content);

                event.consume();
            }
        });

        cell.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                try {
                    if (validDropFromDrag(cell, event)) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                event.consume();
            }
        });

        cell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                try {
                    if (validDropFromDrag(cell, event)) {
                        String sourceAbsolutePath = event.getDragboard().getString();
                        File sourceFile = Paths.get(sourceAbsolutePath).toFile();
                        try {
                            Files.move(sourceFile.toPath(), file.toPath().resolve(sourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
                            FileSystemTreeView fileSystemTreeView = (FileSystemTreeView) cell.getTreeView();
                            refresh(fileSystemTreeView);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(true);

                event.consume();
            }
        });

        System.out.println("Added mouse event");
    }

    protected static void clearDragEvents(TreeCell<File> cell) {
        cell.setOnDragDetected(null);
        cell.setOnDragOver(null);
        cell.setOnDragEntered(null);
        cell.setOnDragExited(null);
        cell.setOnDragDropped(null);
        cell.setOnDragDone(null);
    }

    protected static boolean validDropFromDrag(TreeCell<File> cell, DragEvent event) throws IOException {
        TreeItem<File> treeItem = cell.getTreeItem();
        File destFile = treeItem.getValue();
        boolean validDrop = false;
                /* data is dragged over the target */

                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
        if (event.getGestureSource() != cell &&
                event.getDragboard().hasString()) {
            String sourceAbsolutePath = event.getDragboard().getString();
            File sourceFile = Paths.get(sourceAbsolutePath).toFile();

            validDrop = FileUtils.isValidFileMove(sourceFile, destFile);
        }

        return validDrop;
    }
}

