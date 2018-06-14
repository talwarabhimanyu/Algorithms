/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/
import java.lang.IllegalArgumentException;
import java.lang.Math.abs;
import java.lang.String.valueOf;
import java.lang.Iterable;
import java.util.Iterator;

public class Board {
    private int[][] blocks;
    private int n;
    
    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        if (blocks == null) throw new IlegalArgumentException(); 
        this.blocks = blocks;
        this.n = blocks[0].length;
    }
                                           // (where blocks[i][j] = block in row i, column j)
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
                    manhattanDist += abs(correctI - i) + abs(correctJ - j);
                }
            }
        }
        return manhattanDist;
    }
    public boolean isGoal() {
        // is this board the goal board?
        return (hamming() == 0);
    }
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    public boolean equals(Object y) {
        // does this board equal y?
        for (int i = 0; i < this.n; i++){
            for (int j = 0; j < this.n; j++) {
                if (this.blocks[i][j] != y[i][j]) return false;
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
                    @Override
                    public boolean hasNext(){
                    }
                    @Override
                    public Board next(){
                    }
                };
            }
        };  
    }
    private class BoardIterator implements Iterator<Board> {
        
        @Override
        public boolean hasNext(){
        }
        @Override
        public Board next(){
        }
    }
    public String toString() {
        // string representation of this board (in the output format specified below)
        String strOut;
        for (int i = 0; i < this.n; i++){
            for (int j = 0; j < this.n; j++) {
                if (j != 0) strOut += " ";
                strOut += String.valueOf(this.blocks[i][j]);
            }
            strOut += "\n";
        }
        return strOut;
    }

    public static void main(String[] args) // unit tests (not graded)
}