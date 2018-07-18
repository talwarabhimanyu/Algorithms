/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Arrays;
import java.lang.StringBuilder;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] blocks;
    private final int n;
    private int[] zeros;
    private int manDist;
    
    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) throw new IllegalArgumentException(); 
        this.blocks = this.getBlockCopy(blocks);
        this.n = blocks[0].length;
        this.zeros = new int[]{-1, -1};
        this.manDist = -1;
    }
    private int[] getZeros() {
        if (this.zeros[0] == -1){
            OUTER_LOOP:
            for (int i = 0; i < this.n; i++){
                for (int j = 0; j < this.n; j++) {
                    if (this.blocks[i][j] == 0){
                        this.zeros[0] = i;
                        this.zeros[1] = j;
                        break OUTER_LOOP;
                    }
                }
            }
        }
        return this.zeros;
    }
    private static boolean validIndex(int i, int j, int size) {
        if ((i < 0) || (j < 0) || (i >= size) || (j >= size)) {return false;}
        return true;
    }
    private int neighborCount(){
        int count = 0;
        int[] zeroPosition = this.getZeros();
        if (zeroPosition[0] != 0) {count++;}
        if (zeroPosition[0] != (this.n - 1)) {count++;}
        if (zeroPosition[1] != 0) {count++;}
        if (zeroPosition[1] != (this.n - 1)) {count++;}
        return count;
    }
    private static void swapZero(int[] zeros, int offset_i, int offset_j, int[][] arr){
        int i = zeros[0];
        int j = zeros[1];
        int num = arr[i + offset_i][j + offset_j];
        arr[i][j] = num;
        arr[i + offset_i][j + offset_j] = 0;
    }
    private int[][] getBlockCopy(int[][] blocksToCopy){
        int [][] newBlocks = new int[blocksToCopy.length][];
        for (int i = 0; i < blocksToCopy.length; i++){
            newBlocks[i] = Arrays.copyOf(blocksToCopy[i], blocksToCopy.length);
        }
        return newBlocks;
    }
    public int dimension() {
        // board dimension n
        return this.n;
    }
    public int hamming() {
        // number of blocks out of place
        int countOutOfPlace = 0;
        for (int i = 0; i < this.n; i++){
            for (int j = 0; j < this.n; j++) {
                if ((this.blocks[i][j] != 0) && (this.blocks[i][j] != (i*this.n + j + 1))) countOutOfPlace++;
            }
        }
        return countOutOfPlace;
    }
    public int manhattan() {
        if (this.manDist == -1) {
            this.manDist = this.calcManhattan();
        }
        return this.manDist;
    }
    private int calcManhattan() {
        // sum of Manhattan distances between blocks and goal
        int manhattanDist = 0;
        for (int i = 0; i < this.n; i++){
            for (int j = 0; j < this.n; j++) {
                if (this.blocks[i][j] != 0){
                    int correctJ = (this.blocks[i][j] - 1) % this.n;
                    int correctI = (this.blocks[i][j] - correctJ - 1) / this.n;
                    manhattanDist += Math.abs(correctI - i) + Math.abs(correctJ - j);
                }
            }
        }
        return manhattanDist;
    }
    public boolean isGoal() {
        // is this board the goal board?
        return (this.manhattan() == 0);
    }
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] newBlocks = this.getBlockCopy(this.blocks);
        if (newBlocks[0][0] == 0) {
            int s1 = newBlocks[0][1];
            newBlocks[0][1] = newBlocks[1][1];
            newBlocks[1][1] = s1;
        }
        else if (newBlocks[0][1] == 0){
            int s1 = newBlocks[0][0];
            newBlocks[0][0] = newBlocks[1][0];
            newBlocks[1][0] = s1;
        }
        else {
            int s1 = newBlocks[0][0];
            newBlocks[0][0] = newBlocks[0][1];
            newBlocks[0][1] = s1;
        }
        return new Board(newBlocks);
    }
    public boolean equals(Object y) {
        // does this board equal y?
        if (!(y instanceof Board)) {return false;}
        return Arrays.deepEquals(this.blocks, ((Board)y).blocks);
    }
    public Iterable<Board> neighbors() {
        // all neighboring boards
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    private int[] offset = new int[]{0, 1};
                    private int count = Board.this.neighborCount();
                    private void updateOffset(){
                        int i = this.offset[0];
                        int j = this.offset[1];
                        this.offset[0] = -j;
                        this.offset[1] = i;
                    }
                    @Override
                    public boolean hasNext(){
                        return (this.count != 0);
                    }
                    @Override
                    public Board next(){
                        if (this.hasNext()) {
                            //Board newBoard = new Board(Board.this.getBlockCopy());
                            int[][] newBlocks = Board.this.getBlockCopy(Board.this.blocks);
                            int[] zeros = Board.this.getZeros();
                            int i = zeros[0];
                            int j = zeros[1];
                            while (!validIndex(i + this.offset[0], j + this.offset[1], Board.this.n)) {
                                this.updateOffset();
                            }
                            swapZero(zeros, this.offset[0], this.offset[1], newBlocks);
                            this.count--;
                            this.updateOffset();
                            return new Board(newBlocks);
                        }
                        return null;
                    }
                    @Override
                    public void remove() { /* Not implemented */ }
                };
            }
        };  
    }
    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder sb = new StringBuilder();
        sb.append(this.n);
        sb.append("\n");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (j == 0) {sb.append(" ");}
                else        {sb.append("  ");}
                sb.append(this.blocks[i][j]);
            }
            sb.append(" \n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    // unit tests (not graded)
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.toString());
        Iterator<Board> iter = (initial.neighbors()).iterator();
        while (iter.hasNext()) {
            StdOut.println(iter.next().toString());
        }
    }
}