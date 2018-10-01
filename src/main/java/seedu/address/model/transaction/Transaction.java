package seedu.address.model.transaction;

import java.util.Objects;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {

    private final Type Type;
    private final Amount amount;

    /**
     * Represents a transaction with non null fields {@code Type} and {@code amount}
     * @param Type indicates whether a transaction is a loan/debt
     * @param amount stores the amount lent/owed
     */
    public Transaction(Type Type, Amount amount) {
        this.Type = Type;
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Type, amount);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) other;
        return other == this || (Type.equals(transaction.Type)
                                 && amount.equals(transaction.amount));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
