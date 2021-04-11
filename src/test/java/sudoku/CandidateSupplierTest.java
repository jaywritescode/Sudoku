package sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CandidateSupplierTest {

    Set<Candidate> givens = Set.of(
            new Candidate(1, 2, '2'),
            new Candidate(1, 5, '4'),
            new Candidate(1, 6, '9'),
            new Candidate(1, 7, '3'),
            new Candidate(2, 3, '1'),
            new Candidate(2, 4, '3'),
            new Candidate(2, 8, '8'),
            new Candidate(2, 9, '4'),
            new Candidate(3, 1, '4'),
            new Candidate(3, 5, '1'),
            new Candidate(3, 7, '5'),
            new Candidate(4, 2, '8'),
            new Candidate(4, 3, '2'),
            new Candidate(4, 4, '9'),
            new Candidate(4, 6, '5'),
            new Candidate(5, 2, '1'),
            new Candidate(5, 4, '8'),
            new Candidate(5, 5, '6'),
            new Candidate(6, 1, '9'),
            new Candidate(6, 9, '2'),
            new Candidate(7, 1, '2'),
            new Candidate(7, 3, '5'),
            new Candidate(8, 9, '6'),
            new Candidate(9, 2, '3'),
            new Candidate(9, 7, '9'));
    Sudoku sudoku = Sudoku.create(givens, Set.of('1', '2', '3', '4', '5', '6', '7', '8', '9'), 3, 3);

    @Test
    @DisplayName("it returns the singleton candidate for an initially given cell")
    void getForCellInitiallyGiven() throws Exception {
        CandidateSupplier candidateSupplier = new CandidateSupplier(sudoku);

        assertThat(candidateSupplier.perform(4, 2))
                .singleElement()
                .extracting("row", "column", "digit")
                .containsExactly(4, 2, '8');
    }

    @Test
    @DisplayName("it returns the candidates for an initial blank cell")
    void getForCellInitiallyBlank() throws Exception {
        CandidateSupplier candidateSupplier = new CandidateSupplier(sudoku);
        int row = 6;
        int column = 4;

        assertThat(candidateSupplier.perform(row, column))
                .hasSize(3)
                .allMatch(candidate -> candidate.row == row)
                .allMatch(candidate -> candidate.column == column)
                .extracting(Candidate::getDigit)
                .containsExactlyInAnyOrder('1', '4', '7');
    }
}