package seedu.address.model.transaction;

import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class DeadlineBoundsPredicate implements Predicate<Transaction> {
    /**
     * Represent the type of bound
     */
    public enum BoundType { EARLIEST, LATEST }
    private final Deadline deadline;
    private final BoundType type;

    public DeadlineBoundsPredicate(Deadline deadline, BoundType type) {
        this.deadline = deadline;
        this.type = type;
    }

    @Override
    public boolean test(Transaction transaction) {
        switch (type) {
        case EARLIEST:
            return deadline.compareTo(transaction.getDeadline()) <= 0;
        case LATEST:
            return deadline.compareTo(transaction.getDeadline()) >= 0;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeadlineBoundsPredicate // instanceof handles nulls
                && deadline.equals(((DeadlineBoundsPredicate) other).deadline))
                && type.equals(((DeadlineBoundsPredicate) other).type); // state check
    }

}
