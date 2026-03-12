public class CrosswordPuzzle {
    // TODO: Declare fields here
    private PuzzleGrid grid

    // TODO: Write the constructor
    public CrosswordPuzzle(int rows, int cols)
    //   - create a new PuzzleGrid with the given rows and cols
    //   - store it in grid

    // TODO: Write getter
    public PuzzleGrid getGrid()

    // TODO: Write placeWord(WordEntry entry)
    public void placeWord(WordEntry entry)
    //   - place each character of the word onto the grid one at a time
    //   - use a loop from 0 to entry.length() - 1
    //   - use entry.getWord().charAt(i) to get each character
    //   - if entry.isAcross() is true:  setCell(entry.getRow(), entry.getCol() + i, ch)
    //   - if entry.isAcross() is false: setCell(entry.getRow() + i, entry.getCol(), ch)

    // TODO: Write checkCell(int row, int col, char expectedLetter)
    public boolean checkCell(int row, int col, char expectedLetter)
    //   - get the current character from the grid at (row, col)
    //   - create a WordChecker using that character and expectedLetter
    //   - return the result of isCorrect() on that WordChecker
}