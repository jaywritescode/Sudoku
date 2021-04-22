package sudoku;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.collect.Range;
import csp.ExactCoverProblem;
import org.apache.commons.collections4.set.CompositeSet;

import java.beans.ConstructorProperties;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sudoku {

    private final Set<Candidate> givens;
    private final Set<Character> domain;

    private final int size;
    private final int boxHeight;
    private final int boxWidth;

    private final Set<Box> boxes;

    @ConstructorProperties({"givens", "domain", "boxHeight", "boxWidth"})
    Sudoku(Set<Candidate> givens, Set<Character> domain, int boxHeight, int boxWidth) {
        this.givens = Set.copyOf(givens);
        this.domain = Set.copyOf(domain);
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;

        this.size = boxHeight * boxWidth;
        boxes = createBoxes();
    }

    private Set<Box> createBoxes() {
        BiFunction<HashMap<RowAndColumn, Set<RowAndColumn>>, RowAndColumn, HashMap<RowAndColumn, Set<RowAndColumn>>> reducer = (acc, val) -> {
            acc.put(val, new HashSet<>());
            return acc;
        };
        BinaryOperator<HashMap<RowAndColumn, Set<RowAndColumn>>> combiner = (m, n) -> {
            m.putAll(n);
            return m;
        };
        Map<RowAndColumn, Set<RowAndColumn>> map =
                rowAndColumnStream(boxWidth, boxHeight)
                        .reduce(new HashMap<>(), reducer, combiner);

        this.rowAndColumnStream().forEach(rowAndColumn -> {
            var boxRow = ((int) (rowAndColumn.row - 1) / boxHeight) + 1;
            var boxCol = ((int) (rowAndColumn.column - 1) / boxWidth) + 1;
            var box = RowAndColumn.create(boxRow, boxCol);
            map.get(box).add(rowAndColumn);
        });
        return map.values().stream()
                .map(Box::create)
                .collect(Collectors.toSet());
    }

    private Supplier<Set<Candidate>> solutionSupplier = Suppliers.memoize(
            () -> new ExactCoverProblem<>(candidates(), constraints()) {
                @Override
                public boolean relation(Constraint constraint, Candidate candidate) {
                    return constraint.isSatisfiedBy(candidate);
                }
            }.solve());

    public Set<Candidate> solve() {
        return solutionSupplier.get();
    }

    public Set<Candidate> getGivens() {
        return Set.copyOf(givens);
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    private Set<Candidate> candidates() {
        return rowAndColumnStream()
                .flatMap(this::candidatesForRowAndColumn)
                .collect(Collectors.toSet());
    }

    private Set<Constraint> constraints() {
        Set<Constraint> rowColumnConstraints = IntStream.rangeClosed(1, size)
                .boxed()
                .flatMap(row -> IntStream.rangeClosed(1, size)
                        .mapToObj(column -> RowColumnConstraint.from(row, column)))
                .collect(Collectors.toSet());

        Set<Constraint> rowAndColumnDigitConstraints = domain.stream()
                .flatMap(digit -> IntStream.rangeClosed(1, size)
                        .boxed()
                        .flatMap(i -> Stream.of(
                                RowDigitConstraint.from(i, digit),
                                ColumnDigitConstraint.from(i, digit))))
                .collect(Collectors.toSet());

        Set<Constraint> boxDigitConstraints = boxes.stream()
                .flatMap(box -> domain.stream().map(digit -> BoxDigitConstraint.from(box, digit)))
                .collect(Collectors.toSet());

        return new CompositeSet<>(rowColumnConstraints, rowAndColumnDigitConstraints, boxDigitConstraints);
    }

    private Stream<RowAndColumn> rowAndColumnStream() {
        return Sudoku.rowAndColumnStream(size, size);
    }

    private Stream<Candidate> candidatesForRowAndColumn(RowAndColumn rowAndColumn) {
        return candidatesForRowAndColumn(rowAndColumn.row, rowAndColumn.column);
    }

    private Stream<Candidate> candidatesForRowAndColumn(int row, int column) {
        Optional<Candidate> match = givens.stream()
                .filter(candidate -> candidate.row == row && candidate.column == column)
                .findFirst();
        if (match.isPresent()) {
            return Stream.of(match.get());
        }

        Set<Character> usedDigits = Stream.of(digitsInRow(row), digitsInColumn(column), digitsInBox(row, column))
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
        return domain.stream()
                .filter(Predicate.not(usedDigits::contains))
                .map(digit -> new Candidate(row, column, digit));
    }

    private Stream<Character> digitsInRow(int row) {
        return givens.stream()
                .filter(candidate -> candidate.row == row)
                .map(Candidate::getDigit);
    }

    private Stream<Character> digitsInColumn(int column) {
        return givens.stream()
                .filter(candidate -> candidate.column == column)
                .map(Candidate::getDigit);
    }

    private Stream<Character> digitsInBox(int row, int column) {
        int stack = getStack(column);
        int band = getBand(row);

        return givens.stream()
                .filter(candidate -> getBand(candidate.row) == band && getStack(candidate.column) == stack)
                .map(Candidate::getDigit);
    }

    private int getStack(int column) {
        return (column - 1) / boxWidth;
    }

    private int getBand(int row) {
        return (row - 1) / boxHeight;
    }

    private static Stream<RowAndColumn> rowAndColumnStream(int rows, int columns) {
        Predicate<RowAndColumn> endOfStream = n -> n.row > rows;
        UnaryOperator<RowAndColumn> next = n -> n.column == columns ?
                RowAndColumn.create(n.row + 1, 1) :
                RowAndColumn.create(n.row, n.column + 1);

        return Stream.iterate(RowAndColumn.create(1, 1), Predicate.not(endOfStream), next);
    }

    private static boolean validateInitialState(Set<Candidate> givens, Set<Character> domain, int size) {
        Range<Integer> range = Range.closed(1, size);

        Predicate<Candidate> validRow = candidate -> range.contains(candidate.getRow());
        Predicate<Candidate> validColumn = candidate -> range.contains(candidate.getColumn());
        Predicate<Candidate> validDigit = candidate -> domain.contains(candidate.getDigit());

        return givens.stream().allMatch(validRow.and(validColumn).and(validDigit));
    }

    /**
     * Create a new Sudoku puzzle with sides of a given length and square boxes.
     *
     * @param givens the initial state of the puzzle
     * @param size   the number of rows or the number of columns in the puzzle.
     * @return a Sudoku puzzle
     */
    public static Sudoku create(Set<Candidate> givens, int size) {
        double boxSize = Math.sqrt(size);
        Preconditions.checkArgument(boxSize % 1 == 0,
                "Expected size %s to be a perfect square.", size);

        Set<Character> domain = givens.stream().map(Candidate::getDigit).collect(Collectors.toSet());
        Preconditions.checkArgument(domain.size() == size,
                "Expected %s digits but found %s. Cannot construct domain.",
                size, domain.size());

        Preconditions.checkArgument(validateInitialState(givens, domain, size),
                "Given argument is outside puzzle bounds.");

        return new Sudoku(givens, domain, (int) boxSize, (int) boxSize);
    }

    /**
     * Create a new Sudoku puzzle with the given number of boxes per row and per column.
     *
     * @param givens    the initial state of the column
     * @param domain    the numbers or letters that will appear in this puzzle
     * @param boxHeight the number of boxes per band
     * @param boxWidth  the number of boxes per column
     * @return a Sudoku puzzle
     */
    public static Sudoku create(Set<Candidate> givens, Set<Character> domain, int boxHeight, int boxWidth) {
        int size = boxHeight * boxWidth;

        Preconditions.checkArgument(domain.size() == size,
                "Expected a domain of %s but got %s.", size, domain.size());

        Preconditions.checkArgument(validateInitialState(givens, domain, size),
                "Given argument is outside puzzle bounds.");

        return new Sudoku(givens, domain, boxHeight, boxWidth);
    }

    public static void main(String... args) {
        Set<Candidate> givens = Set.of(
                new Candidate(1, 1, '7'),
                new Candidate(1, 3, '3'),
                new Candidate(1, 7, '2'),
                new Candidate(2, 4, '9'),
                new Candidate(2, 7, '1'),
                new Candidate(3, 1, '2'),
                new Candidate(3, 6, '1'),
                new Candidate(3, 7, '7'),
                new Candidate(3, 9, '6'),
                new Candidate(4, 8, '1'),
                new Candidate(5, 3, '4'),
                new Candidate(5, 9, '8'),
                new Candidate(6, 2, '9'),
                new Candidate(6, 4, '3'),
                new Candidate(6, 6, '8'),
                new Candidate(6, 7, '4'),
                new Candidate(6, 8, '2'),
                new Candidate(7, 4, '2'),
                new Candidate(7, 5, '5'),
                new Candidate(7, 8, '4'),
                new Candidate(8, 2, '3'),
                new Candidate(8, 5, '6'),
                new Candidate(8, 8, '5'),
                new Candidate(9, 3, '1')
        );
        Sudoku puzzle = Sudoku.create(givens, 9);

        var solution = puzzle.solve();

        System.out.println(solution);
    }
}
