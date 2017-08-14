package experiments.directorytree.tree_views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 09/08/2017.
 */
//has knowledge
public class FileSystemTreeView extends TreeView<Path> {
    private List<Path> expandedTreeItemPaths;

    public FileSystemTreeView() {
      super();
      this.expandedTreeItemPaths = new ArrayList<>();
    }

    public List<Path> getExpandedTreeItemPaths() {
        return expandedTreeItemPaths;
    }

    public TreeItem<Path> getNodesForDirectory(File directory) {
        Path dirPath = directory.toPath();
        TreeItem<Path> root = new TreeItem<Path>(dirPath);
        //set expanded if necessary
        if (expandedTreeItemPaths.contains(root.getValue())) {
            root.setExpanded(true);
        }
        ChangeListener<Boolean> changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                BooleanProperty booleanProperty = (BooleanProperty) observable;
                TreeItem<Path> treeItem = (TreeItem<Path>) booleanProperty.getBean();
                if (oldValue == false && newValue == true) {
                    expandedTreeItemPaths.add(treeItem.getValue());
                } else {
                    expandedTreeItemPaths.remove(treeItem.getValue());
                }
            }
        };
        root.expandedProperty().addListener(changeListener);

        //add leaf nodes
        for (File file : directory.listFiles()) {
            if (!file.isHidden()) {
                if (file.isDirectory()) {
                    root.getChildren().add(getNodesForDirectory(file));
                } else {
                    TreeItem<Path> treeItem = new TreeItem<Path>(file.toPath());
                    root.getChildren().add(treeItem);
                }
            }
        }

        return root;
    }

    public boolean hasRoot() {
        return getRoot() != null;
    }

    //fully clear the state of this object
    public void clear() {
        setRoot(null);
        expandedTreeItemPaths.clear();
    }
}
