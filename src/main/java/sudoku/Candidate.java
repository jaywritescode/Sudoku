package sudoku;

import java.util.Objects;

public class Candidate {
    public final int row, column;
    public final char digit;

    public Candidate(int row, int column, char digit) {
        this.row = row;
        this.column = column;
        this.digit = digit;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public char getDigit() {
        return digit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return row == candidate.row && column == candidate.column && digit == candidate.digit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, digit);
    }

    @Override
    public String toString() {
        return String.format("[%d, %d, %s]", row, column, digit);
    }
}
