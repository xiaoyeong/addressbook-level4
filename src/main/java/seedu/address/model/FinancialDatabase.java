package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.UniqueTransactionList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameTransaction comparison)
 */
public class FinancialDatabase implements ReadOnlyFinancialDatabase {

    private final UniquePersonList persons;
    private final UniqueTransactionList transactions;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        transactions = new UniqueTransactionList();
    }

    public FinancialDatabase() {}

    /**
     * Creates an Financial Database using the Transactions in the {@code toBeCopied}
     * @param toBeCopied
     */
    public FinancialDatabase(ReadOnlyFinancialDatabase toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the transactions of the Transaction list with {@code transactions}.
     * {@code transactions} must not contain duplicate transactions.
     */
    public void setTransactions(ObservableList<Transaction> transactions) {
        this.transactions.setTransactions(transactions);
    }

    /**
     * Resets the existing data of this {@code Financial Database} with {@code newData}.
     * @param newData
     */
    public void resetData(ReadOnlyFinancialDatabase newData) {
        requireNonNull(newData);

        setTransactions(newData.getTransactionList());
        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void updatePerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// Transaction-level operations

    /**
     * Returns true if a duplicate transaction is trying to be added to the database.
     */
    public boolean hasTransaction(Transaction transaction) {
        requireNonNull(transaction);
        return transactions.contains(transaction);
    }

    /**
     * Adds a Transaction to the address book.
     * The Transaction must not already exist in the address book.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Replaces the given Transaction {@code target} in the list with {@code editedTransaction}.
     * {@code target} must exist in the address book.
     * The Transaction identity of {@code editedTransaction} must not be the same as another existing Transaction in the address book.
     */
    public void updateTransaction(Transaction target, Transaction editedTransaction) {
        requireNonNull(editedTransaction);

        transactions.setTransaction(target, editedTransaction);
    }

    /**
     * Removes {@code key} from this {@code FinancialDatabase}.
     * {@code key} must exist in the financial database.
     */
    public void removeTransaction(Transaction key) {
        transactions.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " Persons and " +
                transactions.asUnmodifiableObservableList().size() + " Transactions";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Transaction> getTransactionList() {
        return transactions.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FinancialDatabase // instanceof handles nulls
                && transactions.equals(((FinancialDatabase) other).transactions)
                && persons.equals(((FinancialDatabase) other).persons));
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, transactions);
    }
}
