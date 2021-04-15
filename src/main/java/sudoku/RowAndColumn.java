package sudoku;

import java.util.Iterator;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class RowAndColumn {

    public final int row;
    public final int column;

    private RowAndColumn(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Iterator<RowAndColumn> iterator(int size) {
        return new Iterator<>() {
            RowAndColumn value = RowAndColumn.create(1, 0);

            @Override
            public boolean hasNext() {
                return value.row == size && value.column == size;
            }

            @Override
            public RowAndColumn next() {
                return value = (value.column == size ?
                        RowAndColumn.create(value.row + 1, 1) :
                        RowAndColumn.create(value.row, value.column + 1));
            }
        };
    }

    public static Stream<RowAndColumn> rowAndColumnStream(int size) {
        Predicate<sudoku.RowAndColumn> end = n -> n.row > size;
        UnaryOperator<sudoku.RowAndColumn> next = n -> n.column == size ?
                sudoku.RowAndColumn.create(n.row + 1, 1) :
                sudoku.RowAndColumn.create(n.row, n.column + 1);

        return Stream.iterate(sudoku.RowAndColumn.create(1, 1), Predicate.not(end), next);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowAndColumn that = (RowAndColumn) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RowAndColumn.class.getSimpleName() + "[", "]")
                .add("row=" + row)
                .add("column=" + column)
                .toString();
    }

    public static RowAndColumn create(int row, int column) {
        return new RowAndColumn(row, column);
    }
}
