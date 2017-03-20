package experiments.pathregion;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

//http://stackoverflow.com/questions/11075505/get-all-points-within-a-triangle
/**
 * Created by tfisher on 15/03/2017.
 */
public class PathRegion {
    protected int[][] index;
    protected int NO_INDEX = 0;
    protected List<Point2D> region;
    protected Point2D start;
    protected Point2D prev;

    public PathRegion(int width, int height, int x0, int y0) {
        this.index = new int[width][height];
        this.region = new ArrayList<Point2D>();
        this.start = new Point2D(x0, y0);
    }

    //must be called before any updates
    public void prepare() {
        update((int) start.getX(), (int) start.getY());
    }

    public synchronized void update(int newX, int newY) {
        Point2D curr = new Point2D(newX, newY);
        updateRegionAndIndex(newX, newY);

        //if we have at least 3 points
        if (region.size() > 1) {
            Point2D[] p = new Point2D[3];
            sortX(start, prev, curr, p);

            double x1 = p[0].getX();
            double x2 = p[1].getX();
            double x3 = p[2].getX();
            double y1 = -p[0].getY();
            double y2 = -p[1].getY();
            double y3 = -p[2].getY();

            double m1 = (y3 - y1)/(x3-x1);
            double m2 = (y2 - y1)/(x2-x1);
            double m3 = (y3 - y2)/(x3-x2);
            double k1 = y1 - (m1 * x1);
            double k2 = y2 - (m2 * x2);
            double k3 = y3 - (m3 * x3);

            double y_x2 = Math.ceil((m1 * x2) + k1);
            if (y2 < y_x2) {
                //case 1 middle point is lower than L1
                for (int x = (int) x1; x < (int) x2; ++x) {
                    double yMin = ((m2 * x) + k2);
                    double yMax = ((m1 * x) + k1);

                    for (int y = (int) yMin; y < (int) yMax; ++y) {
                        updateRegionAndIndex(x, -y);
                    }
                }

                for (int x = (int) x2; x < (int) x3; ++x) {
                    double yMin = ((m3 * x) + k3);
                    double yMax = ((m1 * x) + k1);

                    for (int y = (int) yMin; y < (int) yMax; ++y) {
                        updateRegionAndIndex(x, -y);
                    }
                }
            } else {
                //case 2 middle point is above than L1
                for (int x = (int) x1; x < (int) x2; ++x) {
                    double yMin = ((m1 * x) + k1);
                    double yMax = ((m2 * x) + k2);

                    for (int y = (int) yMin; y < (int) yMax; ++y) {
                        updateRegionAndIndex(x, -y);
                    }
                }

                for (int x = (int) x2; x < (int) x3; ++x) {
                    double yMin = ((m1 * x) + k1);
                    double yMax = ((m3 * x) + k3);

                    for (int y = (int) yMin; y < (int) yMax; ++y) {
                        updateRegionAndIndex(x, -y);
                    }
                }
            }
        }

        //update previous values
        prev = curr;
    }

    //negative indexes indicate removed point
    protected void updateRegionAndIndex(int x, int y) {
        int currentIndex = index[x][y];

        if (currentIndex == NO_INDEX) {
            Point2D pN = new Point2D(x, y);
            int regionIndex = region.size();
            region.add(pN);
            index[x][y] = regionIndex + 1;
            onAdd(pN);
        } else {
            if (currentIndex < 0) {
                int regionIndex = (currentIndex * -1) - 1;
                Point2D pN = new Point2D(x, y);
                region.set(regionIndex, pN);
                onAdd(pN);
                index[x][y] = currentIndex * -1;
            } else {
                int regionIndex = currentIndex - 1;
                Point2D pN = region.get(regionIndex);
                onRemove(pN);
                region.set(regionIndex, null);
                index[x][y] = currentIndex * -1;
            }
        }
    }

    public void onAdd(Point2D point) {
    }

    public void onRemove(Point2D point) {
    }

    public boolean hasPoint(int x, int y) {
        return index[x][y] != 0;
    }

    public Point2D getPoint(int x, int y) {
        return region.get(index[x][y]);
    }

    public List<Point2D> getRegion() {
        return region;
    }

    //must contain 3 elements
    public static void sortX(Point2D p0, Point2D p1, Point2D p2, Point2D[] points) {
        if (p1.getX() > p0.getX()) {
            points[0] = p0;
            points[1] = p1;
        } else {
            points[0] = p1;
            points[1] = p0;
        }

        if (p2.getX() > points[0].getX() && p2.getX() > points[1].getX()) {
            points[2] = p2;
        } else if (p2.getX() < points[0].getX() && p2.getX() < points[1].getX() ){
            points[2] = points[1];
            points[1] = points[0];
            points[0] = p2;
        } else {
            points[2] = points[1];
            points[1] = p2;
        }
    }
}
