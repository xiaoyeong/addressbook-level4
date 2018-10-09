package seedu.address.model.person;

import java.util.Random;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;


/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidUniqueId(String)}
 */
public class UniqueId {
    public static final String MESSAGE_TRANSACTION_PERSONUID_CONSTRAINTS =
            "Id should contain only numbers";
    public static final String UNIQUEID_VALIDATION_REGEX = "\\d+";
    public final String value;
    /**
     * Constructs a {@code UniqueId}.
     *
     * @param id A valid id.
     */
    public UniqueId(String id) {
        requireNonNull(id);
        checkArgument(isValidUniqueId(id), MESSAGE_TRANSACTION_PERSONUID_CONSTRAINTS);
        value = id;
    }
    public UniqueId() {
        int targetStringLength = 255;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
          char randomLimitedInt;

          int randomNumber = random.nextInt(52);


          if (randomNumber <= 25 ) {
              randomLimitedInt = (char) ('A' + randomNumber ) ;
          } else {
              randomLimitedInt = (char) ('a' + randomNumber - 26);
          }
          buffer.append(randomLimitedInt);
        }
        String generatedString = buffer.toString();
        value = generatedString;
    }

    /**
     * Returns true if a given string is a valid unique id.
     */
    public static boolean isValidUniqueId(String test) {
        return test.matches(UNIQUEID_VALIDATION_REGEX);
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
