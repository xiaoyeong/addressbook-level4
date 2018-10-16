package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Transaction> PREDICATE_MATCHING_NO_PERSONS = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model, List<Transaction> toDisplay) {
        Optional<Predicate<Transaction>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredTransactionList(predicate.orElse(PREDICATE_MATCHING_NO_PERSONS));
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Transaction... toDisplay) {
        setFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Person} equals to {@code other}.
     */
    private static Predicate<Transaction> getPredicateMatching(Transaction other) {
        return person -> person.equals(other);
    }
}
