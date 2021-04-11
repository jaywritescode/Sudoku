package sudoku;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import csp.ExactCoverProblem;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sudoku {

    private final Set<Candidate> givens;
    private final Set<Character> domain;

    private final Set<Candidate> solution = null;

    private final int size;
    private final int boxesPerBand;
    private final int boxesPerStack;

    private final CandidateSupplier candidateSupplier;

    private Sudoku(Set<Candidate> givens, Set<Character> domain, int size, int boxesPerBand, int boxesPerStack) {
        this.givens = Set.copyOf(givens);
        this.domain = Set.copyOf(domain);
        this.size = size;
        this.boxesPerBand = boxesPerBand;
        this.boxesPerStack = boxesPerStack;
        this.candidateSupplier = new CandidateSupplier(this);
    }

    public Set<Candidate> solve() {
        return new ExactCoverProblem<>(candidates(), constraints()) {
            @Override
            public boolean relation(Constraint constraint, Candidate candidate) {
                return constraint.isSatisfiedBy(candidate);
            }
        }.solve();
    }

    public Set<Candidate> getGivens() {
        return givens;
    }

    public Set<Character> getDomain() {
        return domain;
    }

    public int getSize() {
        return size;
    }

    public int getBoxesPerBand() {
        return boxesPerBand;
    }

    public int getBoxesPerStack() {
        return boxesPerStack;
    }

    public int getRowsPerBox() {
        return boxesPerStack;
    }

    public int getColumnsPerBox() {
        return boxesPerBand;
    }

    private Set<Candidate> candidates() {
        Set<Candidate> candidates = new HashSet<>();
        for (var row = 1; row <= size; ++row) {
            for (var column = 1; column <= size; ++column) {
                candidates.addAll(candidateSupplier.perform(row, column));
            }
        }
        return candidates;
    }

    private Set<Constraint> constraints() {
        Set<Constraint> constraints = new HashSet<>();
        for (int row = 1; row <= size; ++row) {
            for (int column = 1; column <= size; ++column) {
                for (char digit : domain) {
                    constraints.add(RowColumnConstraint.from(row, column));
                    constraints.add(RowDigitConstraint.from(row, digit));
                    constraints.add(ColumnDigitConstraint.from(column, digit));
                    // constraints.add(BoxDigitConstraint.from(mapToBox(row, column), digit));
                }
            }
        }
        return constraints;
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
     * @param size the number of rows or the number of columns in the puzzle.
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

        return new Sudoku(givens, domain, size, (int) boxSize, (int) boxSize);
    }

    /**
     * Create a new Sudoku puzzle with the given number of boxes per row and per column.
     *
     * @param givens the initial state of the column
     * @param domain the numbers or letters that will appear in this puzzle
     * @param boxesPerBand the number of boxes per band
     * @param boxesPerStack the number of boxes per column
     * @return a Sudoku puzzle
     */
    public static Sudoku create(Set<Candidate> givens, Set<Character> domain, int boxesPerBand, int boxesPerStack) {
        int size = boxesPerBand * boxesPerStack;

        Preconditions.checkArgument(domain.size() == size,
                "Expected a domain of %s but got %s.", size, domain.size());

        Preconditions.checkArgument(validateInitialState(givens, domain, size),
                "Given argument is outside puzzle bounds.");

        return new Sudoku(givens, domain, size, boxesPerBand, boxesPerStack);
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

        puzzle.solve();

        new SudokuWriter().write(puzzle);
    }
}
