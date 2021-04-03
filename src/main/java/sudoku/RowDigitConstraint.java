package sudoku;

/**
 * A constraint that represents that any given digit must appear exactly once in any given row.
 */
public class RowDigitConstraint extends Constraint {

    public final int row;
    public final char digit;

    private RowDigitConstraint(int row, char digit) {
        this.row = row;
        this.digit = digit;
    }

    @Override
    public boolean isSatisfiedBy(Candidate candidate) {
        return candidate.row == this.row && candidate.digit == this.digit;
    }

    public static RowDigitConstraint from(int row, char digit) {
        return new RowDigitConstraint(row, digit);
    }
}
