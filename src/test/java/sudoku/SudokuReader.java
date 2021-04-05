package sudoku;

import java.util.HashSet;
import java.util.Set;

public class SudokuReader {

    public Sudoku read(String initial) {
        Set<Candidate> givens = new HashSet<>();

        var index = 0;
        for (var row = 1; row <= 9; ++row) {
            for (var column = 1; column <= 9; ++column) {
                char digit = initial.charAt(index++);
                if (digit != '0') {
                    givens.add(new Candidate(row, column, digit));
                }
            }
        }
        return Sudoku.create(givens, 9);
    }
}
