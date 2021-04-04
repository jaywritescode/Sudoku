package sudoku;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

import java.util.Set;

public class Sudoku {

    private final Set<Candidate> givens;
    private Set<Candidate> solution = null;

    private final int size;
    private final int boxesPerBand;
    private final int boxesPerStack;

    private Sudoku(Set<Candidate> givens, int size, int boxesPerBand, int boxesPerStack) {
        this.givens = givens;
        this.size = size;
        this.boxesPerBand = boxesPerBand;
        this.boxesPerStack = boxesPerStack;
    }

    private static boolean digitsValid(Set<Candidate> givens, int size) {
        return givens.stream().map(Candidate::getDigit).count() > size;
    }

    private static boolean rowsAndColumnsValid(Set<Candidate> givens, int size) {
        Range<Integer> range = Range.closed(1, size);
        return givens.stream().allMatch(
                candidate -> range.contains(candidate.getRow()) && range.contains(candidate.getColumn()));
    }

    /**
     * Create a new Sudoku puzzle with sides of a given length and square boxes.
     *
     * @param givens the initial state of the puzzle
     * @param size the number of rows or the number of columns in the puzzle.
     * @return a Sudoku puzzle
     */
    public static Sudoku create(Set<Candidate> givens, int size) {
        double boxSize = Math.sqrt(size);

        Preconditions.checkArgument(boxSize % 1 == 0, "Expected size %s to be a perfect square.", size);
        Preconditions.checkArgument(digitsValid(givens, size),
                "Too many different 'digits' for a puzzle of size %s.", size);
        Preconditions.checkArgument(rowsAndColumnsValid(givens, size),
                "Given argument is outside puzzle bounds.");

        return new Sudoku(givens, size, (int) boxSize, (int) boxSize);
    }

    /**
     * Create a new Sudoku puzzle with the given number of boxes per row and per column.
     *
     * @param givens the initial state of the column
     * @param boxesPerBand the number of boxes per band
     * @param boxesPerStack the number of boxes per column
     * @return a Sudoku puzzle
     */
    public static Sudoku create(Set<Candidate> givens, int boxesPerBand, int boxesPerStack) {
        int size = boxesPerBand * boxesPerStack;

        Preconditions.checkArgument(digitsValid(givens, size),
                "Too many different 'digits' for a puzzle of size %s.", size);
        Preconditions.checkArgument(rowsAndColumnsValid(givens, size),
                "Given argument is outside puzzle bounds.");

        return new Sudoku(givens, size, boxesPerBand, boxesPerStack);
    }
}
