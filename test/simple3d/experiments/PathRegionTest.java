package simple3d.experiments;

import experiments.pathregion.PathRegion;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by tfisher on 07/03/2017.
 */
class PathRegionTest {
    @Test
    void sortX() {
        Point2D p0;
        Point2D p1;
        Point2D p2;
        Point2D[] sortedPoints = new Point2D[3];

        p0 = new Point2D(0,0);
        p1 = new Point2D(1,0);
        p2 = new Point2D(2,0);

        PathRegion.sortX(p0, p1, p2, sortedPoints);

        assertEquals(p0, sortedPoints[0]);
        assertEquals(p1, sortedPoints[1]);
        assertEquals(p2, sortedPoints[2]);


        p0 = new Point2D(-3,-1);
        p1 = new Point2D(-7,3);
        p2 = new Point2D(2,0);

        PathRegion.sortX(p0, p1, p2, sortedPoints);

        assertEquals(p1, sortedPoints[0]);
        assertEquals(p0, sortedPoints[1]);
        assertEquals(p2, sortedPoints[2]);

        p0 = new Point2D(-3,-1);
        p1 = new Point2D(-7,3);
        p2 = new Point2D(-20,30);

        PathRegion.sortX(p0, p1, p2, sortedPoints);

        assertEquals(p2, sortedPoints[0]);
        assertEquals(p1, sortedPoints[1]);
        assertEquals(p0, sortedPoints[2]);

        p0 = new Point2D(0,0);
        p1 = new Point2D(0,0);
        p2 = new Point2D(0,0);

        PathRegion.sortX(p0, p1, p2, sortedPoints);

        assertEquals(p1, sortedPoints[0]);
        assertEquals(p2, sortedPoints[1]);
        assertEquals(p0, sortedPoints[2]);

    }
}