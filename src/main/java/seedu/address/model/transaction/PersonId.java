package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the type of transaction recorded in the database.
 * Guarantees: immutable; is valid as declared in {@link #isValidType(String)}
 */
public class PersonId {
    public static final String MESSAGE_TRANSACTION_PERSONID_CONSTRAINTS =
            "Must be a valid person ID of an existing person.";
    public final String value;

    /**
     * Constructs an {@code Type}.
     *
     * @param id A valid person id.
     */
    public PersonId(String id) {
        requireNonNull(id);
        checkArgument(isValidType(id), MESSAGE_TRANSACTION_PERSONID_CONSTRAINTS);
        this.value = id;
    }


    /**
     * Returns true if a given string is a valid transaction amount.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidType(String test) {
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
        PersonId Type = (PersonId) other;
        return other == this || value.equals(Type.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
