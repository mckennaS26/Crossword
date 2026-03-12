public class WordEntry {
    // TODO: Declare fields here
    private String word;
    private int row;
    private int col;
    private boolean across;

    // TODO: Write the constructor
    public WordEntry(String word, int row, int col, boolean across) {
        this. word = word;
        this.row = row;
        this.col = col;
        this.across = across;
    }
    //   - all fields are set from parameters

    // TODO: Write getters
    public String getWord() {
        return word;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isAcross() {
        return across;
    }

    // TODO: Write length()
    public int length()
    //   - returns the number of characters in word
}