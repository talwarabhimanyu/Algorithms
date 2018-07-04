/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.lang.Iterable;
import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.lang.IllegalArgumentException;

public class Solver {
    private Board goalBoard;
    private boolean solvable;
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {throw new IllegalArgumentException();}
        MinPQ<Board> pqInit = new MinPQ<Board>();
        pqInit.insert(initial);
        MinPQ<Board> pqTwin = new MinPQ<Board>();
        pqTwin.insert(initial.twin());
        MinPQ<Board> pq = pqInit;
        Board minKey = null;
        boolean useInitial = true;
        boolean solved = false;
        while (!solved) {
            minKey = pq.delMin();
            if (minKey.isGoal()) {
                solved = true;
                if (useInitial) {
                    this.solvable = true;
                    this.goalBoard = minKey;
                }
                break;
            }
            Iterator<Board> iter = (minKey.neighbors()).iterator();
            while (iter.hasNext()){
                Board nextBoard = iter.next();
                if (!minKey.equals(nextBoard.getPred())) {
                    nextBoard.setMoves(minKey.getMoves() + 1);
                    nextBoard.setPred(minKey);
                    pq.insert(nextBoard);
                }
            }
            if (useInitial) {pq = pqTwin;}
            else            {pq = pqInit;}
            useInitial = !useInitial;
        }
        if (solved) {
            goalBoard = minKey;
            solvable = true;
        }
        //if initial is not solveable then find a solution to its twin (using A*)
    }
    public boolean isSolvable()            {
    // is the initial board solvable?
        return this.solvable;
    }
    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (this.solvable) {return this.goalBoard.getMoves();}
        else               {return -1;}
    }
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return new Iterable<Board> () {
            @Override
            public Iterator<Board> iterator(){
                return new Iterator<Board> () {
                    private Board currBoard = Solver.this.goalBoard;
                    @Override
                    public boolean hasNext(){
                        return (this.currBoard != null);
                    }
                    @Override
                    public Board next(){
                        if (this.hasNext()) {
                            Board thisBoard = this.currBoard;
                            this.currBoard = this.currBoard.getPred();
                            return thisBoard;
                        }
                        else {return null;}
                    }
                    @Override
                    public void remove(){
                        /*Not implemented*/
                    }
                };
            }
        };
    }
    public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}