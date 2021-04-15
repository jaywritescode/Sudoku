package sudoku;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class SudokuTest {

    @Nested
    class Solve {

        @Test
        @DisplayName("it solves an NxN puzzle with a solution, where N is a perfect square")
        void testSolve() throws Exception {
            var puzzle = "x1x6xxxx9:5xxx27xxx:xxxxx43x1:" +
                         "x75x8xxx3:9xxxx3xxx:1xx94xx7x:" +
                         "2xxxxxx98:8x6xxxxxx:xxxxxx6xx";
            var sudoku = SudokuReader.read(puzzle);

            var solved = "412638759:539127864:768594321:" +
                         "675281943:924763185:183945276:" +
                         "251376498:896412537:347859612";
            var solution = SudokuReader.readCandidates(solved,
                    Set.of('1', '2', '3', '4', '5', '6', '7', '8', '9'));

            assertThat(sudoku.solve()).containsExactlyInAnyOrderElementsOf(solution);
        }
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

    @Test
    @Disabled
    void testDigitsInBox() throws Exception {
        var givens = Set.of(
                new Candidate(1, 1, '6'),
                new Candidate(1, 2, '5'),
                new Candidate(1, 6, '1'),
                new Candidate(2, 3, '2'),
                new Candidate(3, 1, '3'),
                new Candidate(3, 6, '5'),
                new Candidate(4, 2, '6'),
                new Candidate(5, 6, '2'),
                new Candidate(6, 1, '5'),
                new Candidate(6, 3, '1'),
                new Candidate(6, 5, '3'),
                new Candidate(6, 6, '6'));
        Sudoku sudoku = Sudoku.create(givens, Set.of('1', '2', '3', '4', '5', '6'), 2, 3);

        Method method = Sudoku.class.getDeclaredMethod("digitsInBox", int.class, int.class);
        method.setAccessible(true);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 1, 1)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('6', '5', '2');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 1, 2)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('6', '5', '2');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 1, 3)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('6', '5', '2');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 2, 1)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('6', '5', '2');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 2, 2)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('6', '5', '2');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 2, 3)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('6', '5', '2');

            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 1, 4)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 1, 5)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 1, 6)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 2, 4)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 2, 5)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 2, 6)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('1');

            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 3, 1)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 3, 2)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 3, 3)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 4, 1)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 4, 2)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 4, 3)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('3', '6');

            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 3, 4)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 3, 5)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 3, 6)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 4, 4)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 4, 5)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 4, 6)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5');

            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 5, 1)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5', '1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 5, 2)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5', '1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 5, 3)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5', '1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 6, 1)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5', '1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 6, 2)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5', '1');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 6, 3)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('5', '1');

            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 5, 4)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('2', '3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 5, 5)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('2', '3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 5, 6)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('2', '3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 6, 4)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('2', '3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 6, 5)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('2', '3', '6');
            softly.assertThat(((Stream<Character>) method.invoke(sudoku, 6, 6)).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrder('2', '3', '6');
        }
    }

    @Test
    @Disabled
    void testCandidatesForRowAndColumn() throws Exception {
        var givens = Set.of(
                new Candidate(1, 1, '6'),
                new Candidate(1, 2, '5'),
                new Candidate(1, 6, '1'),
                new Candidate(2, 3, '2'),
                new Candidate(3, 1, '3'),
                new Candidate(3, 6, '5'),
                new Candidate(4, 2, '6'),
                new Candidate(5, 6, '2'),
                new Candidate(6, 1, '5'),
                new Candidate(6, 3, '1'),
                new Candidate(6, 5, '3'),
                new Candidate(6, 6, '6'));
        Sudoku sudoku = Sudoku.create(givens, Set.of('1', '2', '3', '4', '5', '6'), 2, 3);

        Method method = Sudoku.class.getDeclaredMethod("candidatesForRowAndColumn", RowAndColumn.class);
        method.setAccessible(true);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            // Candidate for given cell.
            softly.assertThat(((Stream<Candidate>) method.invoke(sudoku, RowAndColumn.create(3, 6))).collect(Collectors.toSet()))
                    .singleElement().extracting("row", "column", "digit").containsExactly(3, 6, '5');

            // Candidate for blank cell.
            softly.assertThat(((Stream<Candidate>) method.invoke(sudoku, RowAndColumn.create(4, 4))).collect(Collectors.toSet()))
                    .allMatch(candidate -> candidate.row == 4)
                    .allMatch(candidate -> candidate.column == 4)
                    .extracting("digit")
                    .containsExactlyInAnyOrder('1', '2', '3', '4');
        }
    }

    @Test
    @Disabled
    void testCandidates() throws Exception {
        Set<Candidate> givens = Set.of(
                new Candidate(1, 2, '4'),
                new Candidate(1, 4, '1'),
                new Candidate(3, 1, '2'),
                new Candidate(4, 2, '1'));
        Sudoku sudoku = Sudoku.create(givens, Set.of('1', '2', '3', '4'), 2, 2);

        Method method = Sudoku.class.getDeclaredMethod("candidates");
        method.setAccessible(true);

        Set<Candidate> expected = Set.of(
                new Candidate(1, 1, '3'),
                new Candidate(1, 2, '4'),
                new Candidate(1, 3, '2'),
                new Candidate(1, 3, '3'),
                new Candidate(1, 4, '1'),

                new Candidate(2, 1, '1'),
                new Candidate(2, 1, '3'),
                new Candidate(2, 2, '2'),
                new Candidate(2, 2, '3'),
                new Candidate(2, 3, '2'),
                new Candidate(2, 3, '3'),
                new Candidate(2, 3, '4'),
                new Candidate(2, 4, '2'),
                new Candidate(2, 4, '3'),
                new Candidate(2, 4, '4'),

                new Candidate(3, 1, '2'),
                new Candidate(3, 2, '3'),
                new Candidate(3, 3, '1'),
                new Candidate(3, 3, '3'),
                new Candidate(3, 3, '4'),
                new Candidate(3, 4, '3'),
                new Candidate(3, 4, '4'),

                new Candidate(4, 1, '3'),
                new Candidate(4, 1, '4'),
                new Candidate(4, 2, '1'),
                new Candidate(4, 3, '2'),
                new Candidate(4, 3, '3'),
                new Candidate(4, 3, '4'),
                new Candidate(4, 4, '2'),
                new Candidate(4, 4, '3'),
                new Candidate(4, 4, '4'));

        assertThat((Set<Candidate>) method.invoke(sudoku)).containsExactlyInAnyOrderElementsOf(expected);
    }

//    @Test
//    void testSudokuReader() throws Exception {
//        var n = "4:2---:--3-:-4--:---1";
//
//        Sudoku puzzle = SudokuReader.read(n);
//
//        fail("fail");
//    }
}