package simple3d.scene_states.edit_mesh;

import experiments.pathregion.PathRegion;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import simple3d.SimpleSubScene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tfisher on 20/03/2017.
 */
public class PathRegionNodeSelect extends PathRegion {
    private List<Node> selectedNodes;
    private List<Node> deselectedNodes;
    private SimpleSubScene simpleSubScene;

    public PathRegionNodeSelect(int width, int height, int x0, int y0, SimpleSubScene simpleSubScene) {
        super(width, height, x0, y0);
        this.selectedNodes = new ArrayList<Node>();
        this.deselectedNodes = new ArrayList<Node>();
        this.simpleSubScene = simpleSubScene;
    }

    @Override
    public void onAdd(Point2D point) {
        Node node = simpleSubScene.getPick(point.getX(), point.getY());

        if (!selectedNodes.contains(node)) {
            selectedNodes.add(node);

            if (deselectedNodes.contains(node)) {
                deselectedNodes.remove(node);
            }
        }
    }

    @Override
    public void onRemove(Point2D point) {
        Node node = simpleSubScene.getPick(point.getX(), point.getY());

        if (!deselectedNodes.contains(node)) {
            deselectedNodes.add(node);
            selectedNodes.remove(node);
        }
    }

    public List<Node> getSelectedNodes() {
        return selectedNodes;
    }
}
