package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Photo;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedTransaction {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Transaction's %s field is missing!";

    @XmlElement(required = true)
    private String amount;
    @XmlElement(required = true)
    private String type;
    @XmlElement(required = true)
    private String deadline;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String photo;
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();


    /**
     * Constructs an XmlAdaptedTransaction.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTransaction() {}

    /**
     * Constructs an {@code XmlAdaptedTransaction} with the given transaction details.
     */
    public XmlAdaptedTransaction(String type, String amount, String deadline, String name, String phone, String email,
                                 String address, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.amount = amount;
        this.type = type;
        this.deadline = deadline;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }

    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTransaction
     */
    public XmlAdaptedTransaction(Transaction source) {
        name = source.getPerson().getName().toString();
        email = source.getPerson().getEmail().value;
        phone = source.getPerson().getPhone().value;
        address = source.getPerson().getAddress().value;
        tagged = source.getPerson().getTags()
                .stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
        amount = source.getAmount().toString();
        type = source.getType().value;
        deadline = source.getDeadline().value;
        photo = source.getPhoto().getPicturePath();
    }

    /**
     * Converts this jaxb-friendly adapted transaction object into the model's Transaction object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted transaction
     */
    public Transaction toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (amount == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Amount.class.getSimpleName()));
        }
        if (!Amount.isValidAmount(amount)) {
            throw new IllegalValueException(Amount.MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS);
        }
        final Amount modelAmount = new Amount(amount);
        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Type.class.getSimpleName()));
        }
        if (!Type.isValidType(type)) {
            throw new IllegalValueException(Type.MESSAGE_TRANSACTION_TYPE_CONSTRAINTS);
        }
        final Type modelType = new Type(type);
        if (deadline == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Deadline.class.getSimpleName()));
        }
        if (!Deadline.matchesDateFormat(deadline)) {
            throw new IllegalValueException(Deadline.MESSAGE_TRANSACTION_DEADLINE_INCORRECT_FORMAT);
        }
        final Deadline modelDeadline = new Deadline(deadline);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Person modelPerson = new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags);

        int a = 0;
        int b = 0;
        Photo phot = new Photo();
        if (this.photo == null) {
            a = 1;
            b = 1;
        }
        if (a == 0 && Photo.isValidPhoto(this.photo) && b == 0) {
            phot = new Photo(this.photo);
        }

        return new Transaction(modelType, modelAmount, modelDeadline, modelPerson, phot);
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
                && Objects.equals(deadline, otherTransaction.deadline)
                && Objects.equals(name, otherTransaction.name)
                && Objects.equals(phone, otherTransaction.phone)
                && Objects.equals(email, otherTransaction.email)
                && Objects.equals(address, otherTransaction.address)
                && Objects.equals(photo, otherTransaction.photo)
                && tagged.equals(otherTransaction.tagged);
    }

    @Override
    public String toString() {
        String tagss = "";
        for (XmlAdaptedTag tag:tagged) {
            try {
                tagss += tag.toModelType().tagName + ";";
            } catch (IllegalValueException ex) {
                ex.printStackTrace();
            }
        }
        return "type: " + type + "\n"
                + "amount: " + amount + "\n"
                + "deadline: " + deadline + "\n"
                + "name: " + name + "\n"
                + "phone: " + phone + "\n"
                + "email: " + email + "\n"
                + "address: " + address + "\n"
                + "tagged: " + tagged;
    }
}
