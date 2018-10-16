package seedu.address.model.transaction;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class MultiFieldPredicate implements Predicate<Transaction> {
    private final List<FieldContainsKeywordsPredicate> predicates;

    public MultiFieldPredicate(List<FieldContainsKeywordsPredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(Transaction person) {
        boolean b = true;
        for (int i = 0; i < predicates.size(); i++) {
            b = b && predicates.get(i).test(person);
        }
        return b;
    }


}
