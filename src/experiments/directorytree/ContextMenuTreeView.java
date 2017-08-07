package experiments.directorytree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeView;

/**
 * Created by tfisher on 07/08/2017.
 */
public class ContextMenuTreeView<T> extends TreeView<T> {
    private ContextMenu contextMenu2;

    //This is necessary for specific control of contextMenu
    public void setContextMenu2(ContextMenu contextMenu) {
        this.contextMenu2 = contextMenu;
    }

    public ContextMenu getContextMenu2() {
        return contextMenu2;
    }

    public boolean hasContextMenu2() {
        return getContextMenu2() != null;
    }

    public void ensureHideContextMenu() {
        if ( hasContextMenu2() && getContextMenu2().isShowing()) {
            getContextMenu2().hide();
        }
    }
}
