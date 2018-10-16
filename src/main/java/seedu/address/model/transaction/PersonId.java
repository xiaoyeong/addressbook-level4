package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the id of transaction associated with a transaction recorded in the database.
 * Guarantees: immutable; is valid as declared in {@link #isValidId(String)}
 */
public class PersonId {
    public static final String MESSAGE_TRANSACTION_PERSONID_CONSTRAINTS =
            "Must be a valid transaction ID of an existing transaction.";
    public final String value;

    /**
     * Constructs an {@code Type}.
     *
     * @param id A valid transaction id.
     */
    public PersonId(String id) {
        requireNonNull(id);
        checkArgument(isValidId(id), MESSAGE_TRANSACTION_PERSONID_CONSTRAINTS);
        this.value = id;
    }


    /**
     * Returns true if string {@test} is a valid identifier.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidId(String test) {
        try {
            return Integer.parseInt(test) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PersonId)) {
            return false;
        }
        PersonId personId = (PersonId) other;
        return other == this || value.equals(personId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
