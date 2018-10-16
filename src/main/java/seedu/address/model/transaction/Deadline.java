package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DateTimeException;
import java.time.LocalDate;


/**
 * Represents the deadline by which payment has to be made
 * Guarantees: immutable; is valid as declared in {@link #isValidDeadline(String)}
 */
public class Deadline {
    public static final String MESSAGE_TRANSACTION_DEADLINE_CONSTRAINTS =
            "The transaction deadline must be a valid date in the future in the DD/MM/YYY format";
    public static final String TRANSACTION_DEADLINE_VALIDATION_REGEX = "\\d{2}/\\d{2}/\\d{4}";
    public final String value;

    /**
     * Constructs an {@code Deadline}.
     *
     * @param deadline A valid transaction deadline.
     */
    public Deadline(String deadline) {
        requireNonNull(deadline);
        checkArgument(isValidDeadline(deadline), MESSAGE_TRANSACTION_DEADLINE_CONSTRAINTS);
        value = deadline;
    }

    /**
     * Returns true if {@code test} is a valid transaction amount.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidDeadline(String test) {
        return test.matches(TRANSACTION_DEADLINE_VALIDATION_REGEX) && checkDate(test);
    }

    /**
     * Checks whether the string {@test} that follow date format is actually a valid date
     * according to the Gregorian Calendar.
     * @param test the command line argument to be parsed
     */
    private static boolean checkDate(String test) {
        int dayOfMonth = Integer.parseInt(test.split("/")[0]);
        int month = Integer.parseInt(test.split("/")[1]);
        int year = Integer.parseInt(test.split("/")[2]);
        try {
            LocalDate date = LocalDate.of(year, month, dayOfMonth);
            return !date.isBefore(LocalDate.now());
        } catch (DateTimeException ex) {
            return false;
        }

    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Type)) {
            return false;
        }
        Type type = (Type) other;
        return other == this || value.equals(type.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
