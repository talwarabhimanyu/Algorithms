/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int grid[];
    private WeightedQuickUnionUF uf;
    private int topNode;
    private int bottomNode;
    private int gridSize;
    private int numOpen;
    
    public Percolation(int n) {
        // Constructor: Create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        // Initialize grid. All elements set to 0. Top/Bottom nodes in first/ last indices.
        grid = new int[n*n + 2];
        gridSize = n;
        topNode = 0;
        bottomNode = n*n + 1;
        numOpen = 0;
        uf = new WeightedQuickUnionUF(n*n + 2);
        // Attach top and bottom nodes to the grid
        for (int i = 1; i <= gridSize; i++) {
            uf.union(topNode, i);
            uf.union(bottomNode, n*(n-1) + i);
        }
    }
    public void open(int row, int col) {
        if ((row < 1) || (row > gridSize) || (col < 1) || (col > gridSize)) {
            throw new java.lang.IllegalArgumentException("Row: " + row + ", Col: " + col);
        }
        // open site (row, col) if it is not open already
        int objIndex = (row - 1)*gridSize +  col;
        if (grid[objIndex] != 1) {
            numOpen += 1;
            grid[objIndex] = 1;
            if (objIndex > gridSize) {
                if (grid[objIndex - gridSize] == 1) {
                    uf.union(objIndex, objIndex - gridSize); // Union with node above
                }
            }
            if (objIndex <= (gridSize*(gridSize -1))) {
                if (grid[objIndex + gridSize] == 1) {
                    uf.union(objIndex, objIndex + gridSize); // Union with node below
                }
            }
            if ((objIndex % gridSize) != 1) {
                if (grid[objIndex - 1] == 1) {
                    uf.union(objIndex, objIndex - 1); // Union with node to left
                }
            }
            if ((objIndex % gridSize) != 0) {
                if (grid[objIndex + 1] == 1) {
                    uf.union(objIndex, objIndex + 1); // Union with node to right
                }
            }
        }
    }
    public boolean isOpen(int row, int col) {
        if ((row < 1) || (row > gridSize) || (col < 1) || (col > gridSize)) {
            throw new java.lang.IllegalArgumentException();
        }
        // is site (row, col) open?
        int objIndex = (row - 1)*gridSize +  col;
        return (grid[objIndex] == 1);
    }
    public boolean isFull(int row, int col) {
        if ((row < 1) || (row > gridSize) || (col < 1) || (col > gridSize)) {
            throw new java.lang.IllegalArgumentException();
        }
        // is site (row, col) full?
        int objIndex = (row - 1)*gridSize +  col;
        return ((uf.connected(topNode, objIndex)) && (grid[objIndex] ==1));
    }
    public int numberOfOpenSites() {
        // number of open sites
        return numOpen;
    }
    public boolean percolates() {              
        // does the system percolate?
        return uf.connected(topNode, bottomNode);
    }

    public static void main(String[] args) {
        // test client (optional)
        
    }
}