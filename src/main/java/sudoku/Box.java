package sudoku;

import java.util.Set;

public class Box {

    private final Set<RowAndColumn> rowAndColumnSet;

    private Box(Set<RowAndColumn> rowAndColumnSet) {
        this.rowAndColumnSet = rowAndColumnSet;
    }

    public boolean containsCell(final int row, final int column) {
        return rowAndColumnSet.stream().anyMatch(n -> n.row == row && n.column == column);
    }

    public static Box create(Set<RowAndColumn> rowAndColumnSet) {
        return new Box(rowAndColumnSet);
    }
}
