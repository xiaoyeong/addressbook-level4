package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;

import seedu.address.model.transaction.Transaction;


/**
 * The API of the Model component.
 */

public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Transaction> PREDICATE_SHOW_ALL_TRANSACTIONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyFinancialDatabase newData);

    /** Returns the Financial Database */
    ReadOnlyFinancialDatabase getFinancialDatabase();

    /**
     * Returns true if a transaction with the same parameters as {@code transaction} exists in the database.
     */
    boolean hasTransaction(Transaction transaction);

    /**
     * Deletes the given transaction from the current transaction list.
     * The transaction must exist in the database.
     */
    void deleteTransaction(Transaction target);

    /**
     * Deletes the given transaction from the past transaction list.
     * The transaction must exist in the database.
     */
    void deletePastTransaction(Transaction target);

    /**
     * Adds the given transaction to the current transaction list.
     * {@code transaction} must not already exist in the database.
     */
    void addTransaction(Transaction transaction);

    /**
     * Add transaction to the past Transactions list.
     * @param person
     */
    void addPastTransaction(Transaction person);

    /**
     * Replaces the given transaction {@code target} with {@code editedTransaction}.
     * {@code target} must exist in the database.
     * The {@code editedTransaction} must not be identical to another existing transaction in the
     * database.
     */
    void updateTransaction(Transaction target, Transaction editedTransaction);

    /** Returns an unmodifiable view of the filtered transaction list */
    ObservableList<Transaction> getFilteredTransactionList();

    /**
     * Does the same thing as getFilteredTransactionList() but for pastTransactions.
     */
    ObservableList<Transaction> getFilteredPastTransactionList();

    /**
     * Updates the filtered transaction list according to the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTransactionList(Predicate<Transaction> predicate);

    /**
     * Does the same thing for updateFilteredTransactionList but for pastTransactions.
     * @param predicate
     */
    void updateFilteredPastTransactionList (Predicate<Transaction> predicate);

    /**
     * Returns true if the model has previous database states to restore.
     */
    boolean canUndoFinancialDatabase();

    /**
     * Returns true if the model has undone database to restore.
     */
    boolean canRedoFinancialDatabase();

    /**
     * Restores the model's database to its previous state.
     */
    void undoFinancialDatabase();

    /**
     * Restores the model's database to its previously undone state.
     */
    void redoFinancialDatabase();

    /**
     * Saves the current database state for undo/redo.
     */
    void commitFinancialDatabase();
}

