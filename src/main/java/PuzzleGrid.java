public class PuzzleGrid {
    // TODO: Declare fields here
    //   private char[][] grid
    //   private int rows
    //   private int cols
    private char[][] grid;
    private int rows;
    private  int cols;

    // TODO: Write the constructor
    public PuzzleGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new char[rows][cols];

    }
    //   public PuzzleGrid(int rows, int cols)
    //   - store rows and cols
    //   - create a new char[rows][cols] and store it in grid
    //   - fill every cell with '#'

    // TODO: Write getters
    //   public int getRows()
    //   public int getCols()
    //   public char getCell(int row, int col)

    // TODO: Write mutator
    //   public void setCell(int row, int col, char value)

    // TODO: Write getRowAsString
    //   public String getRowAsString(int row)
    //   - return all characters in the given row as a single String
    //   - build the string character by character using a loop
}