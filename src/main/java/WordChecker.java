public class WordChecker {
    // TODO: Declare fields here
    private char correctLetter;
    private char guessedLetter;

    // TODO: Write the constructor
    public WordChecker(char correctLetter, char guessedLetter) {
        this.correctLetter = correctLetter;
        this.guessedLetter = guessedLetter;
    }
    //   - both fields are set from parameters

    // TODO: Write getters
    public char getCorrectLetter() {
        return correctLetter;
    }
    public char getGuessedLetter() {
        return guessedLetter;
    }

    // TODO: Write isCorrect()
    public boolean isCorrect() {
        if (guessedLetter == correctLetter) {
            return true;
        }else  {
            return false;
        }
    }
    //   - returns true if guessedLetter == correctLetter, false otherwise

    // TODO: Write getResult()
    public String getResult() {
        if (isCorrect()) {
            return "Correct!";
        }else {
            return "Wrong";
        }
    }
    //   - returns "Correct!" if isCorrect() is true
    //   - returns "Wrong" otherwise
}