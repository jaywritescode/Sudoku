package sudoku;

import com.google.common.base.CharMatcher;

import java.util.HashSet;
import java.util.Set;

public class SudokuReader {

    public static Sudoku read(String puzzle) {
        return read(puzzle, Set.of('1', '2', '3', '4', '5', '6', '7', '8', '9'));
    }

    public static Sudoku read(String puzzle, Set<Character> domain) {
        return Sudoku.create(readCandidates(puzzle, domain, ':'), domain.size());
    }

    public static Sudoku read(String puzzle, Set<Character> domain, int boxWidth, int boxHeight) {
        return Sudoku.create(readCandidates(puzzle, domain, ':'), domain, boxHeight, boxWidth);
    }

    public static Set<Candidate> readCandidates(String puzzle, Set<Character> domain) {
        return readCandidates(puzzle, domain, ':');
    }

    public static Set<Candidate> readCandidates(String puzzle, Set<Character> domain, char rowSeparator) {
        int size = domain.size();

        Set<Candidate> givens = new HashSet<>();

        puzzle = CharMatcher.is(rowSeparator).removeFrom(puzzle);
        var iter = RowAndColumn.iterator(size);

        for (char c : puzzle.toCharArray()) {
            var n = iter.next();
            System.out.println(n);
            if (domain.contains(c)) {
                givens.add(new Candidate(n.row, n.column, c));
            }
        }
        return givens;
    }
}
