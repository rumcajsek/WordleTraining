package pl.jkrol.wordle;

public class PatternLengthOverflowException extends Exception {
    public PatternLengthOverflowException(String message) {
        super(message);
    }
}
