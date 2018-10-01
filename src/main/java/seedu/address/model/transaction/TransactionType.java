package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the type of transaction recorded in the database.
 * Guarantees: immutable; is valid as declared in {@link #isValidTransactionType(String)}
 */
public class TransactionType {
    public static final String MESSAGE_TRANSACTION_TYPE_CONSTRAINTS =
            "The transaction must be either a loan/debt";
    public final String value;

    /**
     * Constructs an {@code TransactionType}.
     *
     * @param transactionType A valid transaction type.
     */
    public TransactionType(String transactionType) {
        requireNonNull(transactionType);
        checkArgument(isValidTransactionType(transactionType), MESSAGE_TRANSACTION_TYPE_CONSTRAINTS);
        value = transactionType;
    }


    /**
     * Returns true if a given string is a valid transaction amount.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidTransactionType(String test) {
        String lower = test.toLowerCase();
        return lower.equals("loan") || lower.equals("debt");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TransactionType)) {
            return false;
        }
        TransactionType transactionType = (TransactionType) other;
        return other == this || value.equals(transactionType.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
