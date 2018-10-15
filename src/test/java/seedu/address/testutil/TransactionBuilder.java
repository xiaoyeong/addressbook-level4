package seedu.address.testutil;

import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.PersonId;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 *
 */
public class TransactionBuilder {
    public static final String DEFAULT_TYPE = "Loan";
    public static final String DEFAULT_AMOUNT = "SGD 10.00";
    public static final String DEFAULT_PERSONID = "1";
    public static final String DEFAULT_DEADLINE = "12/11/2018";

    private Type type;
    private Amount amount;
    private PersonId personId;
    private Deadline deadline;

    public TransactionBuilder() {
        type = new Type(DEFAULT_TYPE);
        amount = new Amount(DEFAULT_AMOUNT);
        personId = new PersonId(DEFAULT_PERSONID);
        deadline = new Deadline(DEFAULT_DEADLINE);
    }

    /**
     * Initializes the TransactionBuilder with the data of {@code transactionToCopy}.
     */
    public TransactionBuilder(Transaction transactionToCopy) {
        type = transactionToCopy.getType();
        amount = transactionToCopy.getAmount();
        personId = transactionToCopy.getPersonId();
    }

    /**
     * Sets the {@code Type} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withType(String type) {
        this.type = new Type(type);
        return this;
    }

    /**
     * Sets the {@code Amount} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withAmount(String amount) {
        this.amount = new Amount(amount);
        return this;
    }

    /**
     * Sets the {@code personId} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPersonId(String personId) {
        this.personId = new PersonId(personId);
        return this;
    }

    public Transaction build() {
        return new Transaction(type, amount, personId, deadline);
    }
}
