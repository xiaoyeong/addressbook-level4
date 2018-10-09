package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.FinancialDatabaseChangedEvent;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedFinancialDatabase versionedFinancialDatabase;
    private final FilteredList<Transaction> filteredTransactions;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyFinancialDatabase addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedFinancialDatabase = new VersionedFinancialDatabase(addressBook);
        filteredTransactions = new FilteredList<>(versionedFinancialDatabase.getTransactionList());
        filteredPersons = new FilteredList<>(versionedFinancialDatabase.getPersonList());
    }

    public ModelManager() {
        this(new FinancialDatabase(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyFinancialDatabase newData) {
        versionedFinancialDatabase.resetData(newData);
        indicateFinancialDatabaseChanged();
    }

    @Override
    public ReadOnlyFinancialDatabase getFinancialDatabase() {
        return versionedFinancialDatabase;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateFinancialDatabaseChanged() {
        raise(new FinancialDatabaseChangedEvent(versionedFinancialDatabase));
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedFinancialDatabase.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedFinancialDatabase.removePerson(target);
        indicateFinancialDatabaseChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedFinancialDatabase.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateFinancialDatabaseChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedFinancialDatabase.updatePerson(target, editedPerson);
        indicateFinancialDatabaseChanged();
    }

    @Override
    public boolean hasTransaction(Transaction person) {
        requireNonNull(person);
        return versionedFinancialDatabase.hasTransaction(person);
    }

    @Override
    public void deleteTransaction(Transaction target) {
        versionedFinancialDatabase.removeTransaction(target);
        indicateFinancialDatabaseChanged();
    }

    @Override
    public void addTransaction(Transaction person) {
        versionedFinancialDatabase.addTransaction(person);
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        indicateFinancialDatabaseChanged();
    }

    @Override
    public void updateTransaction(Transaction target, Transaction editedTransaction) {
        requireAllNonNull(target, editedTransaction);

        versionedFinancialDatabase.updateTransaction(target, editedTransaction);
        indicateFinancialDatabaseChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Filtered Transaction List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Transaction} backed by the internal list of
     * {@code versionedFinancialDatabase}
     */
    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return FXCollections.unmodifiableObservableList(filteredTransactions);
    }

    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        filteredTransactions.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoFinancialDatabase() {
        return versionedFinancialDatabase.canUndo();
    }

    @Override
    public boolean canRedoFinancialDatabase() {
        return versionedFinancialDatabase.canRedo();
    }

    @Override
    public void undoFinancialDatabase() {
        versionedFinancialDatabase.undo();
        indicateFinancialDatabaseChanged();
    }

    @Override
    public void redoFinancialDatabase() {
        versionedFinancialDatabase.redo();
        indicateFinancialDatabaseChanged();
    }

    @Override
    public void commitFinancialDatabase() {
        versionedFinancialDatabase.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedFinancialDatabase.equals(other.versionedFinancialDatabase)
                && filteredTransactions.equals(other.filteredTransactions);
    }

}
