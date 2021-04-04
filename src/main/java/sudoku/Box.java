package sudoku;

import com.google.common.collect.Range;

public class Box {

    public final Range<Integer> rows;
    public final Range<Integer> columns;

    private Box(Range<Integer> rows, Range<Integer> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public boolean containsCell(final int row, final int column) {
        return rows.contains(row) && columns.contains(column);
    }

    public static Box create(Range<Integer> rows, Range<Integer> columns) {
        return new Box(rows, columns);
    }
}
