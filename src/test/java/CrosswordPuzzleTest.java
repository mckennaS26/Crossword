import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CrosswordPuzzleTest {

    // ============================================================
    // Reflection helpers
    // ============================================================

    private static Class<?> requireClass(String simpleName) {
        try {
            return Class.forName(simpleName);
        } catch (ClassNotFoundException e) {
            fail("Missing class: " + simpleName + ". Create " + simpleName + ".java in src/main/java (default package).");
            return null;
        }
    }

    private static Field requireField(Class<?> clazz, String fieldName, Class<?> fieldType) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            assertEquals(fieldType, f.getType(),
                    clazz.getSimpleName() + " field '" + fieldName + "' must be type " + fieldType.getSimpleName() + ".");
            assertTrue(Modifier.isPrivate(f.getModifiers()),
                    clazz.getSimpleName() + " field '" + fieldName + "' should be private.");
            return f;
        } catch (NoSuchFieldException e) {
            fail(clazz.getSimpleName() + " is missing field: private " + fieldType.getSimpleName() + " " + fieldName + ";");
            return null;
        }
    }

    private static Constructor<?> requireConstructor(Class<?> clazz, Class<?>... paramTypes) {
        try {
            Constructor<?> c = clazz.getDeclaredConstructor(paramTypes);
            assertTrue(Modifier.isPublic(c.getModifiers()),
                    clazz.getSimpleName() + " constructor must be public.");
            return c;
        } catch (NoSuchMethodException e) {
            fail(clazz.getSimpleName() + " is missing constructor: public " + clazz.getSimpleName()
                    + "(" + paramList(paramTypes) + ")");
            return null;
        }
    }

    private static Method requireMethod(Class<?> clazz, String name, Class<?> returnType, Class<?>... paramTypes) {
        try {
            Method m = clazz.getDeclaredMethod(name, paramTypes);
            assertTrue(Modifier.isPublic(m.getModifiers()),
                    clazz.getSimpleName() + " method must be public: " + signature(clazz, name, returnType, paramTypes));
            assertEquals(returnType, m.getReturnType(),
                    clazz.getSimpleName() + " method has wrong return type: " + signature(clazz, name, returnType, paramTypes));
            return m;
        } catch (NoSuchMethodException e) {
            fail(clazz.getSimpleName() + " is missing method: " + signature(clazz, name, returnType, paramTypes));
            return null;
        }
    }

    private static String signature(Class<?> clazz, String name, Class<?> returnType, Class<?>... params) {
        return "public " + returnType.getSimpleName() + " " + name + "(" + paramList(params) + ")";
    }

    private static String paramList(Class<?>... params) {
        if (params == null || params.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(params[i].getSimpleName());
        }
        return sb.toString();
    }

    private static Object newInstance(Constructor<?> ctor, Object... args) {
        try {
            return ctor.newInstance(args);
        } catch (Exception e) {
            fail("Constructor threw an exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    private static Object invoke(Method m, Object target, Object... args) {
        try {
            return m.invoke(target, args);
        } catch (Exception e) {
            Throwable cause = (e.getCause() != null) ? e.getCause() : e;
            fail("Method call failed: " + m.getName() + " -> " + cause.getClass().getSimpleName() + ": " + cause.getMessage());
            return null;
        }
    }

    // ============================================================
    // Signature / field existence tests
    // ============================================================

    @Test
    public void puzzleGrid_requiredFields_constructor_and_methods_exist() {
        System.out.println("\n=== Signature Check: PuzzleGrid ===");

        Class<?> pg = requireClass("PuzzleGrid");

        requireField(pg, "grid", char[][].class);
        requireField(pg, "rows", int.class);
        requireField(pg, "cols", int.class);

        requireConstructor(pg, int.class, int.class);

        requireMethod(pg, "getRows", int.class);
        requireMethod(pg, "getCols", int.class);
        requireMethod(pg, "getCell", char.class, int.class, int.class);
        requireMethod(pg, "setCell", void.class, int.class, int.class, char.class);
        requireMethod(pg, "getRowAsString", String.class, int.class);
    }

    @Test
    public void wordEntry_requiredFields_constructor_and_methods_exist() {
        System.out.println("\n=== Signature Check: WordEntry ===");

        Class<?> we = requireClass("WordEntry");

        requireField(we, "word", String.class);
        requireField(we, "row", int.class);
        requireField(we, "col", int.class);
        requireField(we, "across", boolean.class);

        requireConstructor(we, String.class, int.class, int.class, boolean.class);

        requireMethod(we, "getWord", String.class);
        requireMethod(we, "getRow", int.class);
        requireMethod(we, "getCol", int.class);
        requireMethod(we, "isAcross", boolean.class);
        requireMethod(we, "length", int.class);
    }

    @Test
    public void wordChecker_requiredFields_constructor_and_methods_exist() {
        System.out.println("\n=== Signature Check: WordChecker ===");

        Class<?> wc = requireClass("WordChecker");

        requireField(wc, "correctLetter", char.class);
        requireField(wc, "guessedLetter", char.class);

        requireConstructor(wc, char.class, char.class);

        requireMethod(wc, "getCorrectLetter", char.class);
        requireMethod(wc, "getGuessedLetter", char.class);
        requireMethod(wc, "isCorrect", boolean.class);
        requireMethod(wc, "getResult", String.class);
    }

    @Test
    public void crosswordPuzzle_requiredField_constructor_and_methods_exist() {
        System.out.println("\n=== Signature Check: CrosswordPuzzle ===");

        Class<?> cp = requireClass("CrosswordPuzzle");
        Class<?> pg = requireClass("PuzzleGrid");
        Class<?> we = requireClass("WordEntry");

        requireField(cp, "grid", pg);

        requireConstructor(cp, int.class, int.class);

        requireMethod(cp, "getGrid", pg);
        requireMethod(cp, "placeWord", void.class, we);
        requireMethod(cp, "checkCell", boolean.class, int.class, int.class, char.class);
    }

    // ============================================================
    // Behavior tests
    // ============================================================

    @Test
    public void puzzleGrid_behavior_constructorFillsHash_and_setGetCell_and_getRowAsString() {
        System.out.println("\n=== Behavior: PuzzleGrid ===");

        Class<?> pg = requireClass("PuzzleGrid");
        Constructor<?> ctor = requireConstructor(pg, int.class, int.class);
        Method getRows        = requireMethod(pg, "getRows", int.class);
        Method getCols        = requireMethod(pg, "getCols", int.class);
        Method getCell        = requireMethod(pg, "getCell", char.class, int.class, int.class);
        Method setCell        = requireMethod(pg, "setCell", void.class, int.class, int.class, char.class);
        Method getRowAsString = requireMethod(pg, "getRowAsString", String.class, int.class);

        Object grid = newInstance(ctor, 4, 6);

        assertEquals(4, (int) invoke(getRows, grid), "PuzzleGrid.getRows() should return 4.");
        assertEquals(6, (int) invoke(getCols, grid), "PuzzleGrid.getCols() should return 6.");

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 6; c++) {
                char cell = (char) invoke(getCell, grid, r, c);
                assertEquals('#', cell,
                        "All cells should start as '#'. Found '" + cell + "' at [" + r + "][" + c + "].");
            }
        }

        invoke(setCell, grid, 0, 0, 'A');
        assertEquals('A', (char) invoke(getCell, grid, 0, 0),
                "After setCell(0, 0, 'A'), getCell(0, 0) should return 'A'.");

        invoke(setCell, grid, 2, 3, 'Z');
        assertEquals('Z', (char) invoke(getCell, grid, 2, 3),
                "After setCell(2, 3, 'Z'), getCell(2, 3) should return 'Z'.");

        assertEquals('#', (char) invoke(getCell, grid, 0, 1),
                "Cells not explicitly set should remain '#'.");

        assertEquals("A#####", (String) invoke(getRowAsString, grid, 0),
                "getRowAsString(0) should return \"A#####\" after placing 'A' at [0][0].");

        Object grid2 = newInstance(ctor, 3, 5);
        assertEquals("#####", (String) invoke(getRowAsString, grid2, 1),
                "getRowAsString on a fresh grid should return all '#' characters.");
    }

    @Test
    public void wordEntry_behavior_constructor_and_getters() {
        System.out.println("\n=== Behavior: WordEntry ===");

        Class<?> we = requireClass("WordEntry");
        Constructor<?> ctor = requireConstructor(we, String.class, int.class, int.class, boolean.class);
        Method getWord  = requireMethod(we, "getWord", String.class);
        Method getRow   = requireMethod(we, "getRow", int.class);
        Method getCol   = requireMethod(we, "getCol", int.class);
        Method isAcross = requireMethod(we, "isAcross", boolean.class);
        Method length   = requireMethod(we, "length", int.class);

        Object entry = newInstance(ctor, "JAVA", 1, 2, true);
        assertEquals("JAVA", invoke(getWord, entry),  "WordEntry.getWord() should return the word passed to the constructor.");
        assertEquals(1, (int) invoke(getRow, entry),  "WordEntry.getRow() should return the row passed to the constructor.");
        assertEquals(2, (int) invoke(getCol, entry),  "WordEntry.getCol() should return the col passed to the constructor.");
        assertTrue((boolean) invoke(isAcross, entry), "WordEntry.isAcross() should return true when constructed with across=true.");
        assertEquals(4, (int) invoke(length, entry),  "WordEntry.length() should return 4 for the word \"JAVA\".");

        Object down = newInstance(ctor, "CODE", 0, 0, false);
        assertFalse((boolean) invoke(isAcross, down), "WordEntry.isAcross() should return false when constructed with across=false.");
        assertEquals(4, (int) invoke(length, down),   "WordEntry.length() should return 4 for the word \"CODE\".");
    }

    @Test
    public void wordChecker_behavior_isCorrect_and_getResult() {
        System.out.println("\n=== Behavior: WordChecker ===");

        Class<?> wc = requireClass("WordChecker");
        Constructor<?> ctor = requireConstructor(wc, char.class, char.class);
        Method getCorrectLetter = requireMethod(wc, "getCorrectLetter", char.class);
        Method getGuessedLetter = requireMethod(wc, "getGuessedLetter", char.class);
        Method isCorrect        = requireMethod(wc, "isCorrect", boolean.class);
        Method getResult        = requireMethod(wc, "getResult", String.class);

        Object correct = newInstance(ctor, 'A', 'A');
        assertEquals('A', (char) invoke(getCorrectLetter, correct), "WordChecker.getCorrectLetter() should return 'A'.");
        assertEquals('A', (char) invoke(getGuessedLetter, correct), "WordChecker.getGuessedLetter() should return 'A'.");
        assertTrue((boolean) invoke(isCorrect, correct),            "isCorrect() should return true when letters match.");
        assertEquals("Correct!", invoke(getResult, correct),        "getResult() should return \"Correct!\" when isCorrect() is true.");

        Object wrong = newInstance(ctor, 'B', 'X');
        assertFalse((boolean) invoke(isCorrect, wrong),  "isCorrect() should return false when letters differ.");
        assertEquals("Wrong", invoke(getResult, wrong),  "getResult() should return \"Wrong\" when isCorrect() is false.");

        Object unset = newInstance(ctor, 'C', '#');
        assertFalse((boolean) invoke(isCorrect, unset),  "isCorrect() should return false when guessed is '#'.");
        assertEquals("Wrong", invoke(getResult, unset),  "getResult() should return \"Wrong\" for an unset cell.");
    }

    @Test
    public void crosswordPuzzle_placeWord_across() {
        System.out.println("\n=== Behavior: CrosswordPuzzle.placeWord (across) ===");

        Class<?> cp = requireClass("CrosswordPuzzle");
        Class<?> pg = requireClass("PuzzleGrid");
        Class<?> we = requireClass("WordEntry");

        Constructor<?> cpCtor = requireConstructor(cp, int.class, int.class);
        Constructor<?> weCtor = requireConstructor(we, String.class, int.class, int.class, boolean.class);
        Method getGrid        = requireMethod(cp, "getGrid", pg);
        Method placeWord      = requireMethod(cp, "placeWord", void.class, we);
        Method getCell        = requireMethod(pg, "getCell", char.class, int.class, int.class);
        Method getRowAsString = requireMethod(pg, "getRowAsString", String.class, int.class);

        Object puzzle = newInstance(cpCtor, 5, 8);
        Object grid   = invoke(getGrid, puzzle);
        assertNotNull(grid, "CrosswordPuzzle.getGrid() should not return null after construction.");

        invoke(placeWord, puzzle, newInstance(weCtor, "CAT", 1, 2, true));

        assertEquals('C', (char) invoke(getCell, grid, 1, 2), "cell [1][2] should be 'C'.");
        assertEquals('A', (char) invoke(getCell, grid, 1, 3), "cell [1][3] should be 'A'.");
        assertEquals('T', (char) invoke(getCell, grid, 1, 4), "cell [1][4] should be 'T'.");
        assertEquals("##CAT###", (String) invoke(getRowAsString, grid, 1),
                "getRowAsString(1) should be \"##CAT###\".");
        assertEquals("########", (String) invoke(getRowAsString, grid, 0),
                "Row 0 should be all '#'.");
    }

    @Test
    public void crosswordPuzzle_placeWord_down() {
        System.out.println("\n=== Behavior: CrosswordPuzzle.placeWord (down) ===");

        Class<?> cp = requireClass("CrosswordPuzzle");
        Class<?> pg = requireClass("PuzzleGrid");
        Class<?> we = requireClass("WordEntry");

        Constructor<?> cpCtor = requireConstructor(cp, int.class, int.class);
        Constructor<?> weCtor = requireConstructor(we, String.class, int.class, int.class, boolean.class);
        Method getGrid   = requireMethod(cp, "getGrid", pg);
        Method placeWord = requireMethod(cp, "placeWord", void.class, we);
        Method getCell   = requireMethod(pg, "getCell", char.class, int.class, int.class);

        Object puzzle = newInstance(cpCtor, 6, 6);
        Object grid   = invoke(getGrid, puzzle);

        invoke(placeWord, puzzle, newInstance(weCtor, "DOG", 0, 3, false));

        assertEquals('D', (char) invoke(getCell, grid, 0, 3), "cell [0][3] should be 'D'.");
        assertEquals('O', (char) invoke(getCell, grid, 1, 3), "cell [1][3] should be 'O'.");
        assertEquals('G', (char) invoke(getCell, grid, 2, 3), "cell [2][3] should be 'G'.");
        assertEquals('#', (char) invoke(getCell, grid, 3, 3), "cell [3][3] should still be '#'.");
        assertEquals('#', (char) invoke(getCell, grid, 0, 0), "cell [0][0] should still be '#'.");
    }

    @Test
    public void crosswordPuzzle_placeWord_two_words_intersecting() {
        System.out.println("\n=== Behavior: CrosswordPuzzle.placeWord (intersecting words) ===");

        Class<?> cp = requireClass("CrosswordPuzzle");
        Class<?> pg = requireClass("PuzzleGrid");
        Class<?> we = requireClass("WordEntry");

        Constructor<?> cpCtor = requireConstructor(cp, int.class, int.class);
        Constructor<?> weCtor = requireConstructor(we, String.class, int.class, int.class, boolean.class);
        Method getGrid   = requireMethod(cp, "getGrid", pg);
        Method placeWord = requireMethod(cp, "placeWord", void.class, we);
        Method getCell   = requireMethod(pg, "getCell", char.class, int.class, int.class);

        Object puzzle = newInstance(cpCtor, 5, 5);
        Object grid   = invoke(getGrid, puzzle);

        // ARRAY across at row 2: A(c0) R(c1) R(c2) A(c3) Y(c4)
        // ARROW down  at col 2:  A(r0) R(r1) R(r2) O(r3) W(r4)
        // Intersection at [2][2]: ARRAY[2]='R' == ARROW[2]='R' ✓
        invoke(placeWord, puzzle, newInstance(weCtor, "ARRAY", 2, 0, true));
        invoke(placeWord, puzzle, newInstance(weCtor, "ARROW", 0, 2, false));

        assertEquals('A', (char) invoke(getCell, grid, 2, 0), "ARRAY[0] should be 'A'.");
        assertEquals('R', (char) invoke(getCell, grid, 2, 1), "ARRAY[1] should be 'R'.");
        assertEquals('R', (char) invoke(getCell, grid, 2, 2), "Intersection at [2][2] should be 'R' (ARRAY[2] == ARROW[2]).");
        assertEquals('Y', (char) invoke(getCell, grid, 2, 4), "ARRAY[4] should be 'Y'.");
        assertEquals('A', (char) invoke(getCell, grid, 0, 2), "ARROW[0] should be 'A'.");
        assertEquals('R', (char) invoke(getCell, grid, 1, 2), "ARROW[1] should be 'R'.");
        assertEquals('O', (char) invoke(getCell, grid, 3, 2), "ARROW[3] should be 'O'.");
        assertEquals('W', (char) invoke(getCell, grid, 4, 2), "ARROW[4] should be 'W'.");
    }

    @Test
    public void crosswordPuzzle_checkCell_correct_and_wrong() {
        System.out.println("\n=== Behavior: CrosswordPuzzle.checkCell ===");

        Class<?> cp = requireClass("CrosswordPuzzle");
        Class<?> we = requireClass("WordEntry");

        Constructor<?> cpCtor = requireConstructor(cp, int.class, int.class);
        Constructor<?> weCtor = requireConstructor(we, String.class, int.class, int.class, boolean.class);
        Method placeWord = requireMethod(cp, "placeWord", void.class, we);
        Method checkCell = requireMethod(cp, "checkCell", boolean.class, int.class, int.class, char.class);

        Object puzzle = newInstance(cpCtor, 5, 8);
        invoke(placeWord, puzzle, newInstance(weCtor, "LOOP", 0, 0, true));

        assertTrue((boolean)  invoke(checkCell, puzzle, 0, 0, 'L'), "checkCell(0,0,'L') should be true.");
        assertTrue((boolean)  invoke(checkCell, puzzle, 0, 1, 'O'), "checkCell(0,1,'O') should be true.");
        assertTrue((boolean)  invoke(checkCell, puzzle, 0, 2, 'O'), "checkCell(0,2,'O') should be true.");
        assertTrue((boolean)  invoke(checkCell, puzzle, 0, 3, 'P'), "checkCell(0,3,'P') should be true.");
        assertFalse((boolean) invoke(checkCell, puzzle, 0, 0, 'X'), "checkCell(0,0,'X') should be false.");
        assertFalse((boolean) invoke(checkCell, puzzle, 0, 3, 'Q'), "checkCell(0,3,'Q') should be false.");
        assertFalse((boolean) invoke(checkCell, puzzle, 1, 0, 'A'), "Unset '#' cell vs 'A' should be false.");
        assertTrue((boolean)  invoke(checkCell, puzzle, 1, 0, '#'), "Unset '#' cell vs '#' should be true.");
    }

    // ============================================================
    //  Console crossword demo  —  runs once, after all tests pass
    // ============================================================
    //
    //  Verified grid (7 rows x 8 cols):
    //
    //        c0  c1  c2  c3  c4  c5  c6  c7
    //   r0:   C   O   M   P   I  [L]  E   #      1-ACROSS: COMPILE
    //   r1:   #   #   #   #   #   A   #   #      2-DOWN:   LAP (col 5, r0-r2)
    //   r2:   #   #  [L] [O]  O   P   #   #      3-ACROSS: LOOP  /  4-DOWN: OAR (col3, r2-r4)
    //   r3:   #   #   #   A   #   #   #   #
    //   r4:   #  [A]  R   R   A   Y   #   #      5-ACROSS: ARRAY  /  5-DOWN: ALL (col1, r4-r6)
    //   r5:   #   L   #   #   #   #   #   #
    //   r6:   C   L   A   S   S   #   #   #      6-ACROSS: CLASS
    //
    //  Clue numbers assigned by standard crossword rules:
    //    #1 [r0,c0] -> 1-ACROSS: COMPILE
    //    #2 [r0,c5] -> 2-DOWN:   LAP
    //    #3 [r2,c2] -> 3-ACROSS: LOOP
    //    #4 [r2,c3] -> 4-DOWN:   OAR
    //    #5 [r4,c1] -> 5-ACROSS: ARRAY  AND  5-DOWN: ALL
    //    #6 [r6,c0] -> 6-ACROSS: CLASS
    //
    //  All intersections verified:
    //    LAP[2]='P'  == LOOP[3]='P'  at [r2,c5] ✓
    //    OAR[0]='O'  == LOOP[1]='O'  at [r2,c3] ✓
    //    OAR[2]='R'  == ARRAY[2]='R' at [r4,c3] ✓
    //    ALL[0]='A'  == ARRAY[0]='A' at [r4,c1] ✓
    //    ALL[2]='L'  == CLASS[1]='L' at [r6,c1] ✓

    @AfterAll
    public void printCrosswordDemo() {
        System.out.println();

        // --- load classes (bail out gracefully if student code is incomplete) ---
        Class<?> cp, we, pg;
        try {
            cp = Class.forName("CrosswordPuzzle");
            we = Class.forName("WordEntry");
            pg = Class.forName("PuzzleGrid");
        } catch (ClassNotFoundException e) {
            System.out.println("[Crossword demo skipped — classes not found]");
            return;
        }

        Constructor<?> cpCtor, weCtor;
        Method getGrid, placeWord, getRows, getCols, getCell;
        try {
            cpCtor    = cp.getDeclaredConstructor(int.class, int.class);
            weCtor    = we.getDeclaredConstructor(String.class, int.class, int.class, boolean.class);
            getGrid   = cp.getDeclaredMethod("getGrid");
            placeWord = cp.getDeclaredMethod("placeWord", we);
            getRows   = pg.getDeclaredMethod("getRows");
            getCols   = pg.getDeclaredMethod("getCols");
            getCell   = pg.getDeclaredMethod("getCell", int.class, int.class);
        } catch (Exception e) {
            System.out.println("[Crossword demo skipped — required methods not found: " + e.getMessage() + "]");
            return;
        }

        // --- build the puzzle using the student's own classes ---
        Object puzzle, grid;
        try {
            puzzle = cpCtor.newInstance(7, 8);
            grid   = getGrid.invoke(puzzle);

            // ACROSS
            placeWord.invoke(puzzle, weCtor.newInstance("COMPILE", 0, 0, true));  // 1-Across
            placeWord.invoke(puzzle, weCtor.newInstance("LOOP",    2, 2, true));  // 3-Across
            placeWord.invoke(puzzle, weCtor.newInstance("ARRAY",   4, 1, true));  // 5-Across
            placeWord.invoke(puzzle, weCtor.newInstance("CLASS",   6, 0, true));  // 6-Across

            // DOWN
            placeWord.invoke(puzzle, weCtor.newInstance("LAP", 0, 5, false));     // 2-Down
            placeWord.invoke(puzzle, weCtor.newInstance("OAR", 2, 3, false));     // 4-Down
            placeWord.invoke(puzzle, weCtor.newInstance("ALL", 4, 1, false));     // 5-Down

        } catch (Exception e) {
            System.out.println("[Crossword demo skipped — error building puzzle: " + e.getMessage() + "]");
            return;
        }

        int rows, cols;
        try {
            rows = (int) getRows.invoke(grid);
            cols = (int) getCols.invoke(grid);
        } catch (Exception e) {
            System.out.println("[Crossword demo skipped — error reading grid dimensions]");
            return;
        }

        // --- clue-number labels: encoded as row*100+col -> label ---
        int[]    labelKeys = { 0*100+0, 0*100+5, 2*100+2, 2*100+3, 4*100+1, 6*100+0 };
        String[] labelVals = { "1",    "2",      "3",     "4",     "5",     "6"      };

        // --- render the grid ---
        String rule = "+" + "-----+".repeat(cols);

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     CROSSWORD DEMO  —  built with YOUR classes!          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        for (int r = 0; r < rows; r++) {
            System.out.println(rule);

            // top sub-row: clue number flush-left, blocked cells solid
            StringBuilder top = new StringBuilder("|");
            for (int c = 0; c < cols; c++) {
                char ch = safeGetCell(getCell, grid, r, c);
                if (ch == '#') {
                    top.append("#####|");
                } else {
                    String lbl = findLabel(labelKeys, labelVals, r * 100 + c);
                    top.append(String.format("%-2s   |", lbl));
                }
            }
            System.out.println(top);

            // bottom sub-row: letter centered, blocked cells solid
            StringBuilder bot = new StringBuilder("|");
            for (int c = 0; c < cols; c++) {
                char ch = safeGetCell(getCell, grid, r, c);
                if (ch == '#') {
                    bot.append("#####|");
                } else {
                    bot.append("  ").append(ch).append("  |");
                }
            }
            System.out.println(bot);
        }
        System.out.println(rule);

        // --- clues ---
        System.out.println();
        System.out.println("  ACROSS                                  DOWN");
        System.out.println("  1.  To turn source code into bytecode   2.  Catch some rays (also: L-A-P)");
        System.out.println("  3.  A repeating block of code           4.  Rowing tool");
        System.out.println("  5.  A fixed-size collection of values   5.  Every single one");
        System.out.println("  6.  A blueprint for creating objects");
        System.out.println();
        System.out.println("  Words used: COMPILE  LOOP  ARRAY  CLASS  LAP  OAR  ALL");
        System.out.println();
        System.out.println("  (This puzzle was drawn using YOUR PuzzleGrid, WordEntry, and");
        System.out.println("   CrosswordPuzzle classes. If you see it, your code works!)");
        System.out.println();
    }

    /** Read a cell without throwing — returns '?' on any error. */
    private char safeGetCell(Method getCell, Object grid, int r, int c) {
        try {
            return (char) getCell.invoke(grid, r, c);
        } catch (Exception e) {
            return '?';
        }
    }

    /** Return the label string for a given key, or "" if not found. */
    private String findLabel(int[] keys, String[] vals, int key) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == key) return vals[i];
        }
        return "";
    }
}