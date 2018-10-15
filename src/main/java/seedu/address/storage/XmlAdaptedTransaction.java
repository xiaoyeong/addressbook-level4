package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.UniqueId;
import seedu.address.model.transaction.*;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedTransaction {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Transaction's %s field is missing!";

    @XmlElement(required = true)
    private String personid;
    @XmlElement(required = true)
    private String amount;
    @XmlElement(required = true)
    private String type;
    @XmlElement(required = true)
    private String deadline;

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTransaction() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedTransaction(String personid, String amount, String type, String deadline) {
        this.personid = personid;
        this.amount = amount;
        this.type = type;
        this.deadline = deadline;
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedTransaction(Transaction source) {
        personid = source.getPersonId().value;
        amount = source.getAmount().value;
        type = source.getType().value;
        deadline = source.getDeadline().value;
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Transaction toModelType() throws IllegalValueException {

        if (personid == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, UniqueId.class.getSimpleName()));
        }
        if (!PersonId.isValidId(personid)) {
            throw new IllegalValueException(UniqueId.MESSAGE_TRANSACTION_PERSONUID_CONSTRAINTS);
        }
        final PersonId modelPersonId = new PersonId(personid);

        if (amount == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName()));
        }
        if (!Amount.isValidAmount(amount)) {
            throw new IllegalValueException(Amount.MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS);
        }
        final Amount modelAmount = new Amount(amount);

        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Type.class.getSimpleName()));
        }
        if (!Type.isValidType(type)) {
            throw new IllegalValueException(Type.MESSAGE_TRANSACTION_TYPE_CONSTRAINTS);
        }
        final Type modelType = new Type(type);
        if (deadline == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Deadline.class.getSimpleName()));
        }
        if (!Type.isValidType(deadline)) {
            throw new IllegalValueException(Deadline.MESSAGE_TRANSACTION_DEADLINE_CONSTRAINTS);
        }
        final Deadline modelDeadline = new Deadline(deadline);
        return new Transaction(modelType, modelAmount, modelPersonId, modelDeadline);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTransaction)) {
            return false;
        }

        XmlAdaptedTransaction otherTransaction = (XmlAdaptedTransaction) other;
        return Objects.equals(type, otherTransaction.type)
                && Objects.equals(amount, otherTransaction.amount)
                && Objects.equals(personid, otherTransaction.personid)
                && Objects.equals(deadline, otherTransaction.deadline);
    }
}
