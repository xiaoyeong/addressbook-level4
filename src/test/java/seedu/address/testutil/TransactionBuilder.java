package seedu.address.testutil;

import seedu.address.model.person.Person;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;
import seedu.address.model.util.SampleDataUtil;

public class TransactionBuilder {
    public static final String DEFAULT_TYPE = "Loan";
    public static final String DEFAULT_AMOUNT = "SGD 10.00";
    public static final Person DEFAULT_PERSON = SampleDataUtil.getSamplePersons()[0];

    private Type type;
    private Amount amount;
    private Person person;

    public TransactionBuilder() {
        type = new Type(DEFAULT_TYPE);
        amount = new Amount(DEFAULT_AMOUNT);
        person = DEFAULT_PERSON;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code transactionToCopy}.
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
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public TransactionBuilder withAmount(String amount) {
        this.amount = new Amount(amount);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public TransactionBuilder withPerson(Person person) {
        this.amount = new Amount(amount);
        return this;
    }

    public Transaction build() {
        return new Transaction(type, amount, person);
    }
}
