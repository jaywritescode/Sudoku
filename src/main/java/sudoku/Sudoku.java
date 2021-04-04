package sudoku;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import csp.ExactCoverProblem;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sudoku {

    private final Set<Candidate> givens;
    private final Set<Character> domain;

    private Set<Candidate> solution = null;

    private final int size;
    private final int boxesPerBand;
    private final int boxesPerStack;

    private Sudoku(Set<Candidate> givens, Set<Character> domain, int size, int boxesPerBand, int boxesPerStack) {
        this.givens = Set.copyOf(givens);
        this.domain = Set.copyOf(domain);
        this.size = size;
        this.boxesPerBand = boxesPerBand;
        this.boxesPerStack = boxesPerStack;
    }

    public Set<Candidate> solve() {
        return new ExactCoverProblem<>(candidates(), constraints()) {
            @Override
            public boolean relation(Constraint constraint, Candidate candidate) {
                return constraint.isSatisfiedBy(candidate);
            }
        }.solve();
    }

    private Set<Constraint> constraints() {
        Set<Constraint> constraints = new HashSet<>();
        for (int row = 1; row <= size; ++row) {
            for (int column = 1; column <= size; ++column) {
                for (char digit : domain) {
                    constraints.add(RowColumnConstraint.from(row, column));
                    constraints.add(RowDigitConstraint.from(row, digit));
                    constraints.add(ColumnDigitConstraint.from(column, digit));
                    constraints.add(BoxDigitConstraint.from(mapToBox(row, column), digit));
                }
            }
        }
        return constraints;
    };

    private Set<Candidate> candidates() {
        Set<Candidate> candidates = new HashSet<>();
        for (var row = 1; row <= size; ++row) {
            for (var column = 1; column <= size; ++column) {
                Optional<Candidate> given = findGiven(row, column);

                if (given.isPresent()) {
                    candidates.add(given.get());
                }
                else {
                    candidates.addAll(findCandidatesFor(row, column));
                }
            }
        }
        return candidates;
    }

    private Optional<Candidate> findGiven(int row, int column) {
        return givens.stream()
                .filter(candidate -> candidate.row == row && candidate.column == column)
                .findFirst();
    }

    private Collection<Candidate> findCandidatesFor(int row, int column) {
        return domain.stream()
                .map(digit -> new Candidate(row, column, digit))
                .filter(isValidWithGivens)
                .collect(Collectors.toSet());
    }

    private Predicate<Candidate> isValidWithGivens = candidate -> List.of(
            RowDigitConstraint.from(candidate.row, candidate.digit),
            ColumnDigitConstraint.from(candidate.column, candidate.digit),
            BoxDigitConstraint.from(mapToBox(candidate.row, candidate.column), candidate.digit))
            .stream()
            .noneMatch(this::hasGivenSatisfyingConstraint);


    private boolean hasGivenSatisfyingConstraint(Constraint constraint) {
        return givens.stream().anyMatch(constraint::isSatisfiedBy);
    }

    private Box mapToBox(int row, int column) {
        int topLeftRow = (int) (row / boxesPerStack) + 1;
        int topLeftColumn = (int) (column / boxesPerBand) + 1;

        return Box.create(
                Range.closed(topLeftRow, topLeftRow + boxesPerBand),
                Range.closed(topLeftColumn, topLeftColumn * boxesPerStack));
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
}
