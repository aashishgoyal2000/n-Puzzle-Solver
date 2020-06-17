import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
public class Board {
    private final int dim;
    private final int[][] tiles;
    public Board(int[][] tile) {
        if (tile == null) {
            throw new IllegalArgumentException();
        }
        dim = tile.length;
        this.tiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tiles[i][j] = tile[i][j];
            }
        }
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(tiles[i][j] + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    public int dimension() {
        return dim;
    }
    public int hamming() {
        int n = dim, count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (((i * n) + j + 1) != tiles[i][j]) {
                    count++;
                }
            }
        }
        return count - 1;
    }
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tiles[i][j] == 0) continue;
                int rowdiff = Math.abs(row(tiles[i][j]) - row(i*dim + j + 1));
                int coldiff = Math.abs(col(tiles[i][j]) - col(i*dim + j + 1));
                manhattan = manhattan + rowdiff + coldiff;
            }
        }
        return manhattan;
    }
    private int row(int p) {
        return (int) Math.ceil((double) p/(double) dim);
    }
    private int col(int p) {
        if (p % dim == 0) return dim;
        return p % dim;
    }
    public boolean isGoal() {
        int mht = hamming();
        if (mht == 0) {
              return true;
        }  
        return false;
    }
    public boolean equals(Object aab) {
        if (aab == null) {
            return false;
        }
        if (!(aab.getClass().equals(this.getClass()))) {
            return false;
        }
        Board b = (Board) aab;
        boolean ab = true;
        if (this.dimension() != b.dimension()) {
            return false;
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.tiles[i][j] != b.tiles[i][j]) {
                    ab = false;
                    break;
                }
            }
            if (!ab) {
                break;
            }
        }   
        return ab;  
    }
    public Iterable<Board> neighbors() {
        LinkedList<Board> ll = new LinkedList<Board>();
        int row = 0, col = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        if (row - 1 >= 0) {
            ll.add(new Board(exch(row, col, row - 1, col)));
        }
        if (col - 1 >= 0) {
            ll.add(new Board(exch(row, col, row, col - 1)));
        }
        if (row + 1 <= dim - 1) {
            ll.add(new Board(exch(row, col, row + 1, col)));
        }
        if (col + 1 <= dim - 1) {
            ll.add(new Board(exch(row, col, row, col + 1)));
        }
        return ll;
    }
    public Board twin() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim - 1; j++) {
                if ((tiles[i][j] != 0) && (tiles[i][j+1] != 0))
                    return new Board(exch(i, j, i, j + 1));
            }
        }
        return null;
    }
    private int[][] exch(int ai, int aj, int bi, int bj) {
        int[][] copy = copy(tiles);
        int temp = copy[ai][aj];
        copy[ai][aj] = copy[bi][bj];
        copy[bi][bj] = temp;
        return copy;
    }
    private int[][] copy(int[][] tiles1)
    {
        int[][] new1 = new int[tiles1.length][tiles1.length];
        for (int i = 0; i < tiles1.length; i++) {
            for (int j = 0; j < tiles1.length; j++) {
                new1[i][j] = tiles1[i][j];
            }
        }
        return new1;
    }

    public static void main(String[] args)
    {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial.manhattan());
    }

}