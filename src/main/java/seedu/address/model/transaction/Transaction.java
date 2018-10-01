package seedu.address.model.transaction;

import java.util.Objects;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {

    private final TransactionType transactionType;
    private final TransactionAmount transactionAmount;

    /**
     * Represents a transaction with non null fields {@code transactionType} and {@code transactionAmount}
     * @param transactionType indicates whether a transaction is a loan/debt
     * @param transactionAmount stores the amount lent/owed
     */
    public Transaction(TransactionType transactionType, TransactionAmount transactionAmount) {
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionType, transactionAmount);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) other;
        return other == this || (transactionType.equals(transaction.transactionType)
                                 && transactionAmount.equals(transaction.transactionAmount));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
