package seedu.address.model.transaction;

import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class AmountBoundsPredicate implements Predicate<Transaction> {
    /**
     * Represents the type of bound
     */
    public enum BoundType { MIN, MAX }
    private final Amount amount;
    private final BoundType type;

    public AmountBoundsPredicate(Amount amount, BoundType type) {
        this.amount = amount;
        this.type = type;
    }

    @Override
    public boolean test(Transaction transaction) {
        switch (type) {
        case MIN:
            return Amount.compareAmount(amount, transaction.getAmount()) <= 0;
        case MAX:
            return Amount.compareAmount(amount, transaction.getAmount()) >= 0;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AmountBoundsPredicate // instanceof handles nulls
                && amount.equals(((AmountBoundsPredicate) other).amount))
                && type.equals(((AmountBoundsPredicate) other).type); // state check
    }

}
