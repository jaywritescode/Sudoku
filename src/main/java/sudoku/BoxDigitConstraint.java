package sudoku;

/**
 * A constraint that represents that any given digit must appear exactly once in any given box.
 */
public class BoxDigitConstraint extends Constraint {

    public final Box box;
    public final char digit;

    private BoxDigitConstraint(Box box, char digit) {
        this.box = box;
        this.digit = digit;
    }

    @Override
    public boolean isSatisfiedBy(Candidate candidate) {
        return box.containsCell(candidate.getRow(), candidate.getColumn()) && candidate.digit == this.digit;
    }

    public static BoxDigitConstraint from(Box box, char digit) {
        return new BoxDigitConstraint(box, digit);
    }
}
