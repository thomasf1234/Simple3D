package experiments.pathregion;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//http://stackoverflow.com/questions/11075505/get-all-points-within-a-triangle

/**
 * Created by tfisher on 15/03/2017.
 */
public class PathRegionTest extends PathRegion {
    private GraphicsContext gc;

    public PathRegionTest(int width, int height, int x0, int y0, GraphicsContext gc) {
        super(width, height, x0, y0);
        this.gc = gc;
    }

    @Override
    public void onAdd(Point2D point) {
        gc.getPixelWriter().setColor((int) point.getX(), (int) point.getY(), new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.5));
    }

    @Override
    public void onRemove(Point2D point) {
        gc.getPixelWriter().setColor((int) point.getX(), (int) point.getY(), Color.WHITE);
    }
}
