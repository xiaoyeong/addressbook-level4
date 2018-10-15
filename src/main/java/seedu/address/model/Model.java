package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;

import java.util.function.Predicate;

/**
 * The API of the Model component.
 */

public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Transaction> PREDICATE_SHOW_ALL_TRANSACTIONS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyFinancialDatabase newData);

    /** Returns the Financial Database */
    ReadOnlyFinancialDatabase getFinancialDatabase();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void updatePerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);


    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasTransaction(Transaction person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deleteTransaction(Transaction target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addTransaction(Transaction person);

    /**
     * Replaces the given person {@code target} with {@code editedTransaction}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedTransaction} must not be the same as another existing person in the address book.
     */
    void updateTransaction(Transaction target, Transaction editedTransaction);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Transaction> getFilteredTransactionList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTransactionList(Predicate<Transaction> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoFinancialDatabase();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoFinancialDatabase();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoFinancialDatabase();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoFinancialDatabase();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitFinancialDatabase();
}

