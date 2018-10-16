package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.transaction.Transaction;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyFinancialDatabase {

    /**
     * Returns an unmodifiable view of the transactions list.
     * This list will not contain any duplicate transactions.
     */
    ObservableList<Transaction> getTransactionList();

}
