package sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SudokuTest {

    @Test
    void solve() throws Exception {
    }

    @Nested
    class Create {

        @Test
        @DisplayName("it creates a sudoku with square boxes and valid givens")
        void testCreateSquareBoxesAndValid() throws Exception {
            Set<Candidate> givens = Set.of(
                    new Candidate(1, 3, '1'),
                    new Candidate(1, 4, '9'),
                    new Candidate(1, 8, '3'),
                    new Candidate(2, 8, '8'),
                    new Candidate(3, 2, '2'),
                    new Candidate(3, 4, '3'),
                    new Candidate(3, 5, '4'),
                    new Candidate(3, 9, '1'),
                    new Candidate(4, 3, '9'),
                    new Candidate(4, 6, '7'),
                    new Candidate(4, 9, '8'),
                    new Candidate(5, 2, '7'),
                    new Candidate(5, 3, '8'),
                    new Candidate(5, 7, '3'),
                    new Candidate(5, 8, '5'),
                    new Candidate(6, 1, '2'),
                    new Candidate(6, 4, '8'),
                    new Candidate(6, 7, '1'),
                    new Candidate(7, 1, '7'),
                    new Candidate(7, 5, '1'),
                    new Candidate(7, 6, '3'),
                    new Candidate(7, 8, '6'),
                    new Candidate(8, 2, '4'),
                    new Candidate(9, 2, '3'),
                    new Candidate(9, 6, '5'),
                    new Candidate(9, 7, '2')
            );
            assertThatNoException().isThrownBy(() -> Sudoku.create(givens, 9));
        }

        @Test
        @DisplayName("it fails if the boxes aren't square")
        void testCreateNonSquareBoxes() throws Exception {
            Set<Candidate> givens = Set.of(
                    new Candidate(1, 3, '1'),
                    new Candidate(1, 4, '4'),
                    new Candidate(2, 5, '2'),
                    new Candidate(3, 2, '6'),
                    new Candidate(4, 4, '1'),
                    new Candidate(4, 5, '3'),
                    new Candidate(6, 3, '5'),
                    new Candidate(6, 4, '2'),
                    new Candidate(6, 6, '3')
            );
            assertThatIllegalArgumentException().isThrownBy(() -> Sudoku.create(givens, 6));
        }

        @Test
        @DisplayName("it fails if the domain is too small")
        void testCreateDomainTooSmall() throws Exception {
            Set<Candidate> givens = Set.of(
                    new Candidate(1, 2, '2'),
                    new Candidate(2, 4, '4'),
                    new Candidate(3, 1, '2'),
                    new Candidate(4, 3, '1')
            );
            assertThatIllegalArgumentException().isThrownBy(() -> Sudoku.create(givens, 4));
        }

        @Test
        @DisplayName("it fails if given a row outside the bounds of the game")
        void testCreateRowOutsideBounds() throws Exception {
            Set<Candidate> givens = Set.of(
                    new Candidate(5, 3, '1')
            );
            assertThatIllegalArgumentException().isThrownBy(() -> Sudoku.create(givens, 4));
        }

        @Test
        @DisplayName("it fails if given a column outside the bounds of the game")
        void testCreateColumnOutsideBounds() throws Exception {
            Set<Candidate> givens = Set.of(
                    new Candidate(4, 5, '1')
            );
            assertThatIllegalArgumentException().isThrownBy(() -> Sudoku.create(givens, 4));
        }
    }
}