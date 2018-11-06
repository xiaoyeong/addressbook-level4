package seedu.address.model.transaction;

import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Person;
import seedu.address.model.person.Photo;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {
    private final Type type;
    private final Person person;
    private final Deadline deadline;
    private Amount amount;
    private Photo photo;
    private InterestScheme interestScheme;
    private InterestRate interestRate;
    private final Logger logger = LogsCenter.getLogger(getClass());


    /**
     * Represents a transaction with non null fields {@code type}, {@code amount}, {@code deadline}
     * and {@code transaction}
     * @param type type of transaction, either a loan or a debt
     * @param amount the amount lent/owed by creditor/debtor respectively
     * @param deadline the date on which the payment is to be made
     * @param person the transactor loaning/borrowing the {@code amount}
     */
    public Transaction(Type type, Amount amount, Deadline deadline, Person person, Photo photo) {
        this.type = type;
        this.amount = amount;
        this.person = person;
        this.deadline = deadline;
        this.photo = photo;
    }

    public Transaction(Type type, Amount amount, Deadline deadline, Person person) {
        this.type = type;
        this.amount = amount;
        this.person = person;
        this.deadline = deadline;
        this.photo = new Photo();
    }

    public static Transaction copy(Transaction other) {
        return new Transaction(other.type, other.amount, other.deadline, other.person, other.photo);
    }

    public Type getType() {
        return type;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public Person getPerson() {
        return person;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(String photoPath) throws IllegalValueException {
        Photo previousPhoto = this.photo;
        try {
            this.photo = new Photo(photoPath, photoUniqueId());
        } catch (IllegalValueException e) {
            //if could not change photo setback to the previous photo
            this.photo = previousPhoto;
            throw new IllegalValueException("Cannot set new photo");
        }
        logger.info("passsetphoto");

    }

    /**
     * Generates a unique id for each photo.
     */
    public String photoUniqueId() {
        int targetStringLength = 15;
        String value;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            char randomLimitedInt;
            int randomNumber = random.nextInt(52);
            if (randomNumber <= 25) {
                randomLimitedInt = (char) ('A' + randomNumber);
            } else {
                randomLimitedInt = (char) ('a' + randomNumber - 26);
            }
            buffer.append(randomLimitedInt);
        }
        return buffer.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, deadline, person);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) other;
        return other == this || (type.equals(transaction.type)
                && amount.equals(transaction.amount)
                && person.equals(transaction.person)
                && deadline.equals(transaction.deadline));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nPerson Details: ")
               .append(person.toString())
               .append("\nTransaction Details: ")
               .append(" Type: ")
               .append(type)
               .append(" Amount: ")
               .append(amount)
               .append(" Deadline: ")
               .append(deadline);
        return builder.toString();
    }
}
