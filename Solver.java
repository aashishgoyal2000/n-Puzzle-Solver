import edu.princeton.cs.algs4.MinPQ;
import java.util.Iterator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver { 
    private class Node implements Comparable<Node> {
        Board bd;
        Node past = null;
        int manhattan;
        int moves = 0;
        Node(Board bd, int moves, Node past) {
            this.bd = bd;
            this.manhattan = bd.manhattan();
            this.moves = moves;
            this.past = past;
        }
        
        public int getManhattan() {
            return manhattan;
        }

        @Override
        public int compareTo(Node a) {
            int cmp1 = this.manhattan + this.moves;
            int cmp2 = a.manhattan + a.moves;
            if (cmp1 > cmp2) {
                return 1;
            }
            else if (cmp1 < cmp2) {
                return -1;
            }
            return 0;
        }
    }

    private MinPQ<Node> openMP1 = new MinPQ<Node>();
    private MinPQ<Node> openMP2 = new MinPQ<Node>();

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }   
        Node init1 = new Node(initial, 0, null);
        Node init2 = new Node(initial.twin(), 0, null);
        
        openMP1.insert(init1);
        openMP2.insert(init2);       

        while (!openMP1.min().bd.isGoal() && !openMP2.min().bd.isGoal()) {
            Node min1 = openMP1.delMin();
            Node min2 = openMP2.delMin();

            for (Board temp : min1.bd.neighbors()) {
                if (min1.moves == 0) {
                    Node a = new Node(temp, min1.moves + 1, min1);
                    openMP1.insert(a);
                }
                else if (!temp.equals(min1.past.bd)) { 
                    Node a = new Node(temp, min1.moves + 1, min1);
                    openMP1.insert(a);
                }
            }
            
            for (Board temp : min2.bd.neighbors()) {
                if (min2.moves == 0) {
                    Node a = new Node(temp, min2.moves + 1, min2);
                    openMP2.insert(a);
                }
                else if (!temp.equals(min2.past.bd)) {
                    Node a = new Node(temp, min2.moves + 1, min2);
                    openMP2.insert(a);
                }
            }
        }
    }

    public boolean isSolvable() {
        if (openMP1.min().bd.isGoal()) {
            return true;
        }
        return false;
    }

    public int moves() {
        if (isSolvable()) {
            return openMP1.min().moves;
        }
        return -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Node sol = openMP1.min();
        Stack<Board> stackSolution = new Stack<Board>();
        while (sol != null) {
            stackSolution.push(sol.bd);
            sol = sol.past;
        }
        return stackSolution;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}