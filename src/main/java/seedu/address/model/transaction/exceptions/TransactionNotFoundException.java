package seedu.address.model.transaction.exceptions;

/**
 * Signals that the operation is unable to find the specified person.
 */
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
        super("Transaction does not exist in the database");
    }
}
