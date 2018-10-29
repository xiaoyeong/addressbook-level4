package seedu.address.model.transaction;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class MultiFieldPredicate implements Predicate<Transaction> {
    private final List<Predicate<Transaction>> predicates;

    /**
     * Represents the operation type
     */
    public enum OperatorType { AND, OR }
    private final OperatorType opType;


    public MultiFieldPredicate(List<Predicate<Transaction>> predicates, OperatorType type) {
        this.predicates = predicates;
        this.opType = type;
    }

    @Override
    public boolean test(Transaction person) {
        boolean b;
        if (opType == OperatorType.AND) {
            b = true;
            for (int i = 0; i < predicates.size(); i++) {
                b = b && predicates.get(i).test(person);
            }
        } else {
            b = false;
            for (int i = 0; i < predicates.size(); i++) {
                b = b || predicates.get(i).test(person);
            }
        }
        return b;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MultiFieldPredicate // instanceof handles nulls
                && predicates.equals(((MultiFieldPredicate) other).predicates)
                && opType.equals(((MultiFieldPredicate) other).opType)
                ); // state check
    }



}
