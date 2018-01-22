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
        for (int i = 0; i < numPoints - 3; i++){
            Point p = points[i];
            if (p == null) throw new IllegalArgumentException("Point is null.");
            Arrays.sort(points, i + 1, numPoints, p.slopeOrder());
            int j = i + 1;
            while (j < numPoints) {
                int lo = j;
                double slope = p.slopeTo(points[j++]);
                int iCount  = 1;
                while ((j < numPoints) && (p.slopeTo(points[j]) == slope)){
                    j++;
                    iCount++;
                }
                if (iCount >= 3) {
                    AddLine(points, i, lo, j - 1);
                }
            }
        }
    }
    private void AddLine(Point[] p, int first, int lo, int hi) {
        Point min = p[first];
        Point max = p[first];
        for (int i = lo; i <= hi; i++) {
            if (min.compareTo(p[i]) > 0) min = p[i];
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