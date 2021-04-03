package sudoku;

/**
 * A constraint that represents that each (row ⨉ column) must contain exactly one digit.
 */
public class RowColumnConstraint extends Constraint {

    public final int row, column;

    private RowColumnConstraint(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean isSatisfiedBy(Candidate candidate) {
        return candidate.row == this.row && candidate.column == this.column;
    }

    public static RowColumnConstraint from(int row, int column) {
        return new RowColumnConstraint(row, column);
    }
}
