/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.lang.Math;
import java.lang.String;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Arrays;

public class Board implements Comparable<Board>{
    private int[][] blocks;
    private int n;
    private int[] zeros;
    private int numMoves;
    private Board pred;
    private int manDist;
    
    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) throw new IllegalArgumentException(); 
        this.blocks = blocks;
        this.n = blocks[0].length;
        this.zeros = new int[]{-1, -1};
        this.numMoves = 0;
        this.pred = null;
        this.manDist = this.calcManhattan();
    }
    public int[][] getBlocks(){
        return this.blocks;
    }
    public int[] getZeros() {
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
    public void setZeros(int i, int j){
        this.zeros = new int[]{i, j};
    }
    public void setMoves(int i) {
        this.numMoves = i;
    }
    public int getMoves() {
        return this.numMoves;
    }
    public void setPred(Board pred){
        this.pred = pred;
    }
    public Board getPred(){
        return this.pred;
    }
    public static boolean validIndex(int i, int j, int size) {
        if ((i < 0) || (j < 0) || (i >= size) || (j >= size)) {return false;}
        return true;
    }
    public int neighborCount(){
        int count = 0;
        int[] zeros = this.getZeros();
        if (zeros[0] != 0) {count++;}
        if (zeros[0] != this.n) {count++;}
        if (zeros[1] != 0) {count++;}
        if (zeros[1] != this.n) {count++;}
        return count;
    }
    public static void swapZero(int[] zeros, int offset_i, int offset_j, int[][] arr){
        int i = zeros[0];
        int j = zeros[1];
        int num = arr[i + offset_i][j + offset_j];
        arr[i][j] = num;
        arr[i + offset_i][j + offset_j] = 0;
    }
    public int[][] getBlockCopy(){
        int [][] newBlocks = new int[this.blocks.length][];
        for (int i = 0; i < this.blocks.length; i++){
            newBlocks[i] = Arrays.copyOf(this.blocks[i], this.blocks.length);
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
        return (this.manDist == 0);
    }
    public int compareTo(Board that) {
        if (that == null) throw new NullPointerException();
        int distThat = that.manhattan();
        int distThis = this.manhattan();
        if (distThis > distThat)       return +1;
        else if (distThis < distThat)  return -1;
        else                           return (1 - 1)/1;
    }
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] newBlocks = this.getBlockCopy();
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
        int[][] blocks = ((Board)y).getBlocks();
        for (int i = 0; i < this.n; i++){
            for (int j = 0; j < this.n; j++) {
                if (this.blocks[i][j] != blocks[i][j]) return false;
            }
        }
        return true;
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
                            int[][] newBlocks = Board.this.getBlockCopy();
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
        String strOut = "";
        for (int i = 0; i < this.n; i++){
            for (int j = 0; j < this.n; j++) {
                if (j != 0) strOut += " ";
                strOut += String.valueOf(this.blocks[i][j]);
            }
            strOut += "\n";
        }
        return strOut;
    }

    public static void main(String[] args) {
    // unit tests (not graded)
        int n = 3;
        int[][] block1 = new int[n][n];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                block1[i][j] = i*n + j + 1;
            }
        }
        block1[n-1][n-1] = 0;
        // Test 1
        System.out.println("Test 1 ...");
        Board board1 = new Board(block1);
        System.out.println(board1.toString());
        if (board1.isGoal()) {System.out.println("Goal!");}
        else                 {System.out.printf("No goal. Man. = %d\n", board1.manhattan());}
        Iterator<Board> iter1 = (board1.neighbors()).iterator();
        while(iter1.hasNext()){
            Board neighbor = iter1.next();
            System.out.println(neighbor.toString());
            if (neighbor.isGoal()) {System.out.println("Goal!");}
            else                   {System.out.printf("No goal. Man. = %d\n", neighbor.manhattan());}
        }
        // Test 2
        System.out.println("Test 2 ...");
        int[][] block2 = new int[block1.length][];
        for (int i = 0; i < block1.length; i++){
            block2[i] = Arrays.copyOf(block1[i], block1.length);
        }
        block2[2][2] = 6;
        block2[1][2] = 5;
        block2[1][1] = 0;
        Board board2 = new Board(block2);
        System.out.println(board2.toString());
        if (board2.isGoal()) {System.out.println("Goal!");}
        else                 {System.out.printf("No goal. Man. = %d\n", board2.manhattan());}
        Iterator<Board> iter2 = (board2.neighbors()).iterator();
        while(iter2.hasNext()){
            Board neighbor = iter2.next();
            System.out.println(neighbor.toString());
            if (neighbor.isGoal()) {System.out.println("Goal!");}
            else                   {System.out.printf("No goal. Man. = %d\n", neighbor.manhattan());}
        }
        System.out.println(board1.equals(board1));
        
    }
}