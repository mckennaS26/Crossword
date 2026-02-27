# Crossword Puzzle Mini Project (4 Classes)

## Objective
In this mini project, you will build a simplified crossword puzzle system.

You will practice:
- declaring and using **2D arrays** (`char[][]`)
- writing classes from a specification
- declaring instance variables (fields)
- writing constructors
- implementing methods that read and write 2D array cells
- using loops to traverse 2D arrays
- using control structures (`if`, `else`, comparisons)

No ArrayLists.  
No randomness.  
No input (Scanner).

---

## Files You Will Create / Complete

- `src/main/java/PuzzleGrid.java`
- `src/main/java/WordEntry.java`
- `src/main/java/WordChecker.java`
- `src/main/java/CrosswordPuzzle.java`

---

## The Scenario

A crossword puzzle is played on a rectangular **grid** of cells.  
Each cell holds one character.  
The grid starts filled with `'#'` (black/blocked squares).

A `WordEntry` describes a word to be placed on the grid: its text, starting row, starting column, and direction (across or down).

`PuzzleGrid` manages the 2D character array: placing characters, reading characters, and printing the grid.

`WordChecker` validates whether a completed cell matches the expected letter; it is used to check if a player's guess for a single cell is correct.

`CrosswordPuzzle` ties everything together: it places `WordEntry` objects onto a `PuzzleGrid` and exposes a method to check whether a specific cell's current value is the correct answer letter.

---

# Class Requirements

---

## Class 1: PuzzleGrid

### Fields (instance variables)
- `grid` (char[][]) the 2D array of characters
- `rows` (int) number of rows
- `cols` (int) number of columns

### Constructor
```java
public PuzzleGrid(int rows, int cols)
```
- Sets `rows` and `cols` from parameters
- Creates a new `char[rows][cols]` and stores it in `grid`
- Fills **every** cell with `'#'`

### Methods

```java
public int getRows()
public int getCols()
public char getCell(int row, int col)
public void setCell(int row, int col, char value)
public String getRowAsString(int row)
```

#### Rules
- `getCell(row, col)` returns the character at `grid[row][col]`
- `setCell(row, col, char value)` sets `grid[row][col]` to `value`
- `getRowAsString(row)` returns all characters in that row as a single `String`  
  (hint: build the string character by character using a loop)

---

## Class 2: WordEntry

### Fields (instance variables)
- `word` (String) the word to place
- `row` (int) starting row on the grid
- `col` (int) starting column on the grid
- `across` (boolean) `true` if placed left-to-right, `false` if placed top-to-bottom

### Constructor
```java
public WordEntry(String word, int row, int col, boolean across)
```
- All fields are set from parameters

### Methods
```java
public String getWord()
public int getRow()
public int getCol()
public boolean isAcross()
public int length()
```

#### Rules
- `length()` returns the number of characters in `word`

---

## Class 3: WordChecker

### Fields (instance variables)
- `correctLetter` (char) the expected answer character
- `guessedLetter` (char) what is currently in the cell

### Constructor
```java
public WordChecker(char correctLetter, char guessedLetter)
```
- Both fields are set from parameters

### Methods
```java
public char getCorrectLetter()
public char getGuessedLetter()
public boolean isCorrect()
public String getResult()
```

#### Rules
- `isCorrect()` returns `true` if `guessedLetter == correctLetter`, `false` otherwise
- `getResult()` returns `"Correct!"` if `isCorrect()` is `true`, otherwise returns `"Wrong"`

---

## Class 4: CrosswordPuzzle

### Fields (instance variables)
- `grid` (PuzzleGrid). the puzzle grid

### Constructor
```java
public CrosswordPuzzle(int rows, int cols)
```
- Creates a new `PuzzleGrid` with the given `rows` and `cols` and stores it in `grid`

### Methods
```java
public PuzzleGrid getGrid()
public void placeWord(WordEntry entry)
public boolean checkCell(int row, int col, char expectedLetter)
```

#### placeWord Rules
- Places each character of `entry.getWord()` onto the grid, one letter at a time
- If `entry.isAcross()` is `true`:
    - Place characters left to right: `grid.setCell(entry.getRow(), entry.getCol() + i, ch)` for each index `i`
- If `entry.isAcross()` is `false` (i.e., down):
    - Place characters top to bottom: `grid.setCell(entry.getRow() + i, entry.getCol(), ch)` for each index `i`
- Use a loop from `0` to `entry.length() - 1`
- Hint: use `entry.getWord().charAt(i)` to get each character

#### checkCell Rules
- Creates a `WordChecker` using the character currently in the grid at `(row, col)` and `expectedLetter`
- Returns the result of calling `isCorrect()` on that `WordChecker`

---

## Notes
- Follow the method names and parameter lists exactly.
- Your code will be tested automatically.
- Use `char` (primitive), not `Character`.
- All grid cells start as `'#'` when the `PuzzleGrid` is constructed.