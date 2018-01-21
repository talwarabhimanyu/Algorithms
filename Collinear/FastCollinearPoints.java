/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private int numLines;
    private Stack<LineSegment> lineStack;
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException("Point is null.");
        int numPoints = points.length;
        numLines = 0;
        lineStack = new Stack<LineSegment>();
        for (int i = 0; i < numPoints - 4; i++){
            Point p = points[i];
            if (p == null) throw new IllegalArgumentException("Point is null.");
            Arrays.sort(points, i + 1, numPoints, p.slopeOrder());
            double slope1 = p.slopeTo(points[i + 1]);
            double slope2 = p.slopeTo(points[i + 2]);
            for (int j = i + 1; j < numPoints - 4; j++){
                double slope3 = p.slopeTo(points[j + 2]);
                if ((slope1 == slope2) && (slope1 == slope3)) {
                    AddLine(new Point[]{p, points[j], points[j + 1], points[j + 2]});
                }
                slope1 = slope2;
                slope2 = slope3;
            }
        }
    }
    private void AddLine(Point[] p) {
        Point min = p[0];
        Point max = p[0];
        for (int i = 1; i < p.length; i++) {
            if (min.compareTo(p[i]) >= 0) min = p[i];
            if (max.compareTo(p[i]) < 0) max = p[i];
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
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
}
}