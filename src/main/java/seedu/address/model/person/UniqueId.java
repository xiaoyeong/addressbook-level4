package seedu.address.model.person;

import java.util.Random;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUniqueId(String)}
 */
public class UniqueId {


    public static final String MESSAGE_PHONE_CONSTRAINTS =
            "Phone numbers should only contain numbers, and it should be at least 3 digits long";
    public static final String PHONE_VALIDATION_REGEX = ".*";
    public  String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */

    public UniqueId(String phone) {
        requireNonNull(phone);
        checkArgument(isValidUniqueId(phone), MESSAGE_PHONE_CONSTRAINTS);
        value = phone;
    }

    public UniqueId() {
        int targetStringLength = 255;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            char randomLimitedInt = (char) ('A' + (random.nextInt(20)));
            buffer.append( randomLimitedInt);
        }
        this.value = buffer.toString();

    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidUniqueId(String test) {
        return test.matches(PHONE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueId // instanceof handles nulls
                && value.equals(((UniqueId) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}