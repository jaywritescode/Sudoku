package sudoku;

import com.google.common.collect.Range;

/**
 * A constraint that represents that any given digit must appear exactly once in any given box.
 */
public class BoxDigitConstraint extends Constraint {

    public final Range<Integer> rows, columns;
    public final char digit;

    private BoxDigitConstraint(Range<Integer> rows, Range<Integer> columns, char digit) {
        this.rows = rows;
        this.columns = columns;
        this.digit = digit;
    }

    @Override
    public boolean isSatisfiedBy(Candidate candidate) {
        return rows.contains(candidate.row) && columns.contains(candidate.column) && candidate.digit == this.digit;
    }

    public static BoxDigitConstraint from(Range<Integer> rows, Range<Integer> columns, char digit) {
        return new BoxDigitConstraint(rows, columns, digit);
    }
}
