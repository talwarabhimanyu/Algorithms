/** Abhimanyu Talwar's solutions to Princeton's Algorithms MOOC on Coursera **/
/** https://github.com/talwarabhimanyu/                                     **/

import java.lang.Iterable;
import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

public class Solver {
    private class SearchNode implements Comparable<SearchNode>{
        private Board board;
        private SearchNode parent;
        private SearchNode child;
        private int moves;
        private SearchNode(Board board) {
            this.board = board;
            this.parent = null;
            this.child = null;
            this.moves = 0;
        }
        public int compareTo(SearchNode that) {
            if (that == null) throw new NullPointerException();
            int thisPriority = this.board.manhattan() + this.moves;
            int thatPriority = that.board.manhattan() + that.moves;
            if (thisPriority > thatPriority)       return +1;
            else if (thisPriority < thatPriority)  return -1;
            else                                   return (1 - 1)/1;
        }
        public SearchNode twin() {
            Board twinBoard = this.board.twin();
            return new SearchNode(twinBoard);
        }
    }
    
    private final SearchNode rootNode;
    private boolean solvable;
    private int movesToSolve;
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {throw new IllegalArgumentException();}
        movesToSolve = 0;
        this.rootNode = new SearchNode(initial);
        MinPQ<SearchNode> pqInit = new MinPQ<SearchNode>();
        pqInit.insert(this.rootNode);
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pqTwin.insert(this.rootNode.twin());
        MinPQ<SearchNode> pq = pqInit;
        boolean useInitBoard = true;
        boolean solved = false;
        while (!solved) {
            SearchNode minKey = pq.delMin();
            SearchNode parentNode = minKey.parent;
            if (minKey.board.isGoal()) {
                solved = true;
                if (useInitBoard) {
                    movesToSolve = minKey.moves;
                    this.solvable = true;
                    this.setChildLinks(minKey);
                }
                else {
                    // If we have reached the goal using the Twin, then
                    // the original board was not solvable.
                    this.solvable = false;
                }
                break;
            }
            Iterator<Board> iter = (minKey.board.neighbors()).iterator();
            while (iter.hasNext()){
                Board nextBoard = iter.next();
                // If neighboring board is not the same as parent's board, add it to pq
                if ((parentNode == null) || (!nextBoard.equals(parentNode.board))) {
                    SearchNode childNode = new SearchNode(nextBoard);
                    childNode.parent = minKey;
                    // Moves need to be updated before inserting into the pq
                    childNode.moves = minKey.moves + 1;
                    pq.insert(childNode);
                }                
            }
            if (useInitBoard) {pq = pqTwin;}
            else              {pq = pqInit;}
            // Toggle between the initial board and its twin
            useInitBoard = !useInitBoard;
        }
    }
    private void setChildLinks(SearchNode node) {
        SearchNode parentNode = node.parent;
        SearchNode currNode = node;
        while (parentNode != null) {
            parentNode.child = currNode;
            currNode = parentNode;
            parentNode = currNode.parent;
        }
    }
    public boolean isSolvable()            {
    // is the initial board solvable?
        return this.solvable;
    }
    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if (this.solvable) {return this.movesToSolve;}
        else               {return -1;}
    }
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return new Iterable<Board> () {
            @Override
            public Iterator<Board> iterator(){
                return new Iterator<Board> () {
                    private SearchNode currNode = Solver.this.rootNode;
                    @Override
                    public boolean hasNext(){
                        return (this.currNode != null);
                    }
                    @Override
                    public Board next(){
                        if (this.hasNext()) {
                            Board thisBoard = this.currNode.board;
                            this.currNode = this.currNode.child;
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