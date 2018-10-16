package seedu.address.testutil;

import seedu.address.model.person.Person;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 *
 */
public class TransactionBuilder {
    public static final String DEFAULT_TYPE = "Loan";
    public static final String DEFAULT_AMOUNT = "SGD 10.00";
    public static final Person DEFAULT_PERSON = TypicalPersons.AMY;
    public static final String DEFAULT_DEADLINE = "12/11/2018";

    private Type type;
    private Amount amount;
    private Person person;
    private Deadline deadline;

    public TransactionBuilder() {
        type = new Type(DEFAULT_TYPE);
        amount = new Amount(DEFAULT_AMOUNT);
        person = DEFAULT_PERSON;
        deadline = new Deadline(DEFAULT_DEADLINE);
    }

    /**
     * Initializes the TransactionBuilder with the data of {@code transactionToCopy}.
     */
    public TransactionBuilder(Transaction transactionToCopy) {
        type = transactionToCopy.getType();
        amount = transactionToCopy.getAmount();
        person = transactionToCopy.getPerson();
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
     * Sets the {@code Person} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    public Transaction build() {
        return new Transaction(type, amount, deadline, person);
    }
}
