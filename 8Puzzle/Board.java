/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/
import java.lang.IllegalArgumentException;
import java.lang.Math;
import java.lang.String;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.Arrays;

public class Board {
    private int[][] blocks;
    private int n;
    private int[] zeros;
    
    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) throw new IllegalArgumentException(); 
        this.blocks = blocks;
        this.n = blocks[0].length;
        this.zeros = new int[]{-1, -1};
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
    public boolean validIndex(int i, int j) {
        if ((i < 0) || (j < 0) || (i >= this.n) || (j >= this.n)) {return false;}
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
    public void swapZero(int offset_i, int offset_j){
        int[] zeros = this.getZeros();
        int i = zeros[0];
        int j = zeros[1];
        int num = this.blocks[i + offset_i][j + offset_j];
        this.blocks[i][j] = num;
        this.blocks[i + offset_i][j + offset_j] = 0;
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
        return (hamming() == 0);
    }
    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        Board newBoard = new Board(this.getBlockCopy());
        int[] newZeros = newBoard.getZeros();
        int i = newZeros[0];
        int j = newZeros[1];
        int offset_i = 0;
        int offset_j = 1;
        while (!newBoard.validIndex(i + offset_i, j + offset_j)) {
            int orig_i = offset_i;
            int orig_j = offset_j;
            offset_i = -orig_j;
            offset_j = orig_i;
        }
        newBoard.swapZero(i + offset_i, j + offset_j);
        return newBoard;
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
                            Board newBoard = new Board(Board.this.getBlockCopy());
                            int[] zeros = Board.this.getZeros();
                            int i = zeros[0];
                            int j = zeros[1];
                            while (!newBoard.validIndex(i + this.offset[0], j + this.offset[1])) {
                                this.updateOffset();
                            }
                            newBoard.swapZero(offset[0], offset[1]);
                            this.count--;
                            this.updateOffset();
                            return newBoard;
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
                block1[i][j] = i*n + j;
            }
        }
        Board board1 = new Board(block1);
        System.out.println(board1.toString());
        if (board1.isGoal()) {System.out.println("Goal!");}
        Iterator<Board> iter1 = (board1.neighbors()).iterator();
        while(iter1.hasNext()){
            Board neighbor = iter1.next();
            System.out.println(neighbor.toString());
            if (neighbor.isGoal()) {System.out.println("Goal!");}
            else                   {System.out.println("No goal.");}
        }
    }
}