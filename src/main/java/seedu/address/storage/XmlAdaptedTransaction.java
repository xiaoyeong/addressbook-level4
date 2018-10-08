package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.transaction.*;
import seedu.address.model.tag.Tag;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;
import java.util.stream.Collectors;

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


    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTransaction() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedTransaction(String personid, String amount, String type) {
        this.personid = personid;
        this.amount = amount;
        this.type = type;
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
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Transaction toModelType() throws IllegalValueException {

        if (personid == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PersonId.class.getSimpleName()));
        }
        if (!PersonId.isValidType(personid)) {
            throw new IllegalValueException(PersonId.MESSAGE_TRANSACTION_PERSONID_CONSTRAINTS);
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


        return new Transaction(modelType, modelAmount, modelPersonId);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTransaction)) {
            return false;
        }

        XmlAdaptedTransaction otherPerson = (XmlAdaptedTransaction) other;
        return Objects.equals(type, otherPerson.type)
                && Objects.equals(amount, otherPerson.amount)
                && Objects.equals(personid, otherPerson.personid);
    }
}
