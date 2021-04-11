package sudoku;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Supplies candidates for a standard sudoku.
 */
public class CandidateSupplier {

    private final Sudoku sudoku;

    public CandidateSupplier(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public Set<Candidate> perform(int row, int column) {
        Optional<Candidate> match = sudoku.getGivens().stream()
                .filter(candidate -> candidate.row == row && candidate.column == column)
                .findFirst();
        if (match.isPresent()) {
            return Collections.singleton(match.get());
        }

        Set<Character> usedDigits = Stream.of(digitsInRow(row), digitsInColumn(column), digitsInBox(row, column))
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
        return sudoku.getDomain().stream()
                .filter(Predicate.not(usedDigits::contains))
                .map(digit -> new Candidate(row, column, digit))
                .collect(Collectors.toSet());
    }

    private Stream<Character> digitsInRow(int row) {
        return sudoku.getGivens().stream()
                .filter(candidate -> candidate.row == row)
                .map(Candidate::getDigit);
    }

    private Stream<Character> digitsInColumn(int column) {
        return sudoku.getGivens().stream()
                .filter(candidate -> candidate.column == column)
                .map(Candidate::getDigit);
    }

    private Stream<Character> digitsInBox(int row, int column) {
        int stack = getStack(column);
        int band = getBand(row);

        return sudoku.getGivens().stream()
                .filter(candidate -> getBand(candidate.row) == band && getStack(candidate.column) == stack)
                .map(Candidate::getDigit);
    }

    private int getStack(int column) {
        return (column - 1) / sudoku.getColumnsPerBox();
    }

    private int getBand(int row) {
        return (row - 1) / sudoku.getRowsPerBox();
    }
}
