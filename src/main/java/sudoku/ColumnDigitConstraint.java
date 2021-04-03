package sudoku;

/**
 * A constraint that represents that any given digit must appear exactly once in any given column.
 */
public class ColumnDigitConstraint extends Constraint {

    public final int column;
    public final char digit;

    private ColumnDigitConstraint(int column, char digit) {
        this.column = column;
        this.digit = digit;
    }

    @Override
    public boolean isSatisfiedBy(Candidate candidate) {
        return candidate.column == this.column && candidate.digit == this.digit;
    }

    public static ColumnDigitConstraint from(int column, char digit) {
        return new ColumnDigitConstraint(column, digit);
    }
}
