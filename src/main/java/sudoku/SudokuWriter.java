package sudoku;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.stream.IntStream;

public class SudokuWriter {

    public void write(Sudoku sudoku) {
        write(sudoku, System.out);
    }

    public String row(Sudoku sudoku, int row) {
        StringBuilder sb = new StringBuilder();
//        IntStream.rangeClosed(1, sudoku.getSize())
//                .forEachOrdered(column -> sb.append(sudoku.getGiven(row, column)
//                        .map(candidate -> digit(candidate.getDigit()))
//                        .orElse(StringUtils.SPACE)));
        return sb.append(StringUtils.LF).toString();
    }

    public void write(Sudoku sudoku, PrintStream out) {
        StringBuilder sb = new StringBuilder();

        for (var row = 1; row <= sudoku.getSize(); ++row) {
            sb.append(row(sudoku, row));
        }
        out.println(sb.toString());
    }

    private String digit(char digit) {
        return String.valueOf(digit);
    }
}
