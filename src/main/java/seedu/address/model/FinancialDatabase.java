package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.UniqueTransactionList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameTransaction comparison)
 */
public class FinancialDatabase implements ReadOnlyFinancialDatabase {

    protected final UniqueTransactionList transactions;
    protected final UniqueTransactionList pastTransactions;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        transactions = new UniqueTransactionList();
        pastTransactions = new UniqueTransactionList();
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
     * Replaces the transactions of the Transaction list with {@code transactions}.
     * {@code transactions} must not contain duplicate transactions.
     */
    public void setTransactions(ObservableList<Transaction> transactions, UniqueTransactionList list) {
        list.setTransactions(transactions);
    }

    /**
     * Resets the existing data of this {@code Financial Database} with {@code newData}.
     * @param newData
     */
    public void resetData(ReadOnlyFinancialDatabase newData) {

        setTransactions(newData.getTransactionList(), transactions);
        setTransactions(newData.getPastTransactionList(), pastTransactions);
    }

    //// transaction-level operations


    //// Transaction-level operations

    /**
     * Returns true if a duplicate transaction is trying to be added to the database.
     */
    public boolean hasTransaction(Transaction transaction, UniqueTransactionList list) {
        requireNonNull(transaction);
        return list.contains(transaction);
    }

    /**
     * Adds a Transaction to the address book.
     * The Transaction must not already exist in the address book.
     */
    public void addTransaction(Transaction transaction, UniqueTransactionList list) {
        list.add(transaction);
    }

    /**
     * Replaces the given Transaction {@code target} in the list with {@code editedTransaction}.
     * {@code target} must exist in the address book.
     * The Transaction identity of {@code editedTransaction} must not be the same as another existing Transaction in the
     * address book.
     */
    public void updateTransaction(Transaction target, Transaction editedTransaction) {
        requireNonNull(editedTransaction);

        transactions.setTransaction(target, editedTransaction);
    }

    /**
     * Removes {@code key} from this {@code FinancialDatabase}.
     * {@code key} must exist in the financial database.
     */
    public void removeTransaction(Transaction key, UniqueTransactionList list) {
        list.remove(key);
    }

    /**
     * Retrieves current transactions list
     * @return
     */
    public UniqueTransactionList getCurrentList() {
        return this.transactions;
    }

    /**
     * Retrieves past transactions list
     * @return
     */
    public UniqueTransactionList getPastList() {
        return this.pastTransactions;
    }

    //// util methods

    @Override
    public String toString() {
        return transactions.asUnmodifiableObservableList().size() + " Transactions";
        // TODO: refine later
    }

    @Override
    public ObservableList<Transaction> getTransactionList() {
        return transactions.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Transaction> getPastTransactionList() {
        return pastTransactions.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FinancialDatabase // instanceof handles nulls
                && transactions.equals(((FinancialDatabase) other).transactions));
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactions);
    }

}
