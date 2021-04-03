package sudoku;

public abstract class Constraint {
    public abstract boolean isSatisfiedBy(Candidate candidate);
}
