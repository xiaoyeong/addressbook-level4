package seedu.address.testutil;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;
import seedu.address.model.util.SampleDataUtil;

/**
 *
 */
public class TransactionBuilder {
    private static final String DEFAULT_TYPE = "Loan";
    private static final String DEFAULT_AMOUNT = "SGD 10.00";
    private static final Person DEFAULT_PERSON = TypicalPersons.AMY;
    private static final String DEFAULT_DEADLINE = "25/12/2018";

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
        deadline = transactionToCopy.getDeadline();
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
     * Sets the {@code Deadline} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withDeadline(String deadline) {
        this.deadline = new Deadline(deadline);
        return this;
    }

    /**
     * Sets the {@code Person} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withName(String name) {
        this.person = new Person(new Name(name), this.person.getPhone(), this.person.getEmail(),
                this.person.getAddress(), this.person.getTags());
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withAddress(String address) {
        this.person = new Person(this.person.getName(), this.person.getPhone(), this.person.getEmail(),
                new Address(address), this.person.getTags());
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withEmail(String email) {
        this.person = new Person(this.person.getName(), this.person.getPhone(), new Email(email),
                this.person.getAddress(), this.person.getTags());
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPhone(String phone) {
        this.person = new Person(this.person.getName(), new Phone(phone), this.person.getEmail(),
                this.person.getAddress(), this.person.getTags());
        return this;
    }

    /**
     * Sets the {@code Tags} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withTags(String ... tags) {
        this.person = new Person(this.person.getName(), this.person.getPhone(), this.person.getEmail(),
                this.person.getAddress(), SampleDataUtil.getTagSet(tags));
        return this;
    }

    public Transaction build() {
        return new Transaction(type, amount, deadline, person);
    }
}
