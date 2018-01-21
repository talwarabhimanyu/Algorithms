/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.lang.IllegalArgumentException;
import java.util.Stack;

public class BruteCollinearPoints {
    private int numLines;
    private Stack<LineSegment> lineStack;
    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException("Point is null.");
        numLines = 0;
        lineStack = new Stack<LineSegment>();
        int numPoints = points.length;
        for (int i = 0; i < numPoints - 4; i++) {
            Point point1 = points[i];
            if (point1 == null) throw new IllegalArgumentException("Point is null.");
            for (int j = i + 1; j < numPoints - 3; j++) {
                Point point2 = points[j];
                if (point2 == null) throw new IllegalArgumentException("Point is null.");
                double slope1 = point1.slopeTo(point2);
                for (int k = j + 1; k < numPoints - 2; k++) {
                    Point point3 = points[k];
                    if (point3 == null) throw new IllegalArgumentException("Point is null.");
                    double slope2 = point1.slopeTo(point3);
                    for (int l = k + 1; l < numPoints - 1; l++) {
                        Point point4 = points[l];
                        if (point4 == null) throw new IllegalArgumentException("Point is null.");
                        double slope3 = point1.slopeTo(point4);
                        if ((slope1 == slope2) && (slope1 == slope3)) AddLine(new Point[]{point1, point2, point3, point4});
                    }
                }
            }
        }
    }
    private void AddLine(Point[] p) {
        Point min = p[0];
        Point max = p[0];
        for (int i = 1; i < p.length; i++) {
            if (min.compareTo(p[i]) >= 0) min = p[i];
            if (min.compareTo(p[i]) < 0) max = p[i];
        }
        numLines++;
        lineStack.push(new LineSegment(min, max));
    }
    public int numberOfSegments() {       // the number of line segments
        return numLines;
    }
    public LineSegment[] segments() {                // the line segments
        LineSegment[] lines = new LineSegment[numLines];
        int i = 0;
        while (!lineStack.isEmpty()) {
            lines[i++] = lineStack.pop();
        }
        return lines;
    }
}