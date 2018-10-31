package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.sun.istack.NotNull;

import seedu.address.commons.core.LogsCenter;

/**
 * Represents the deadline by which payment has to be made
 * Guarantees: immutable; is valid as declared in {@link #isValidDeadline(String)}
 */
public class Deadline implements Comparable<Deadline> {
    public static final String MESSAGE_TRANSACTION_DEADLINE_CONSTRAINTS =
            "The transaction deadline must be a valid date in the future following the DD/MM/YYYY format";
    public static final String TRANSACTION_DEADLINE_VALIDATION_REGEX = "\\d{1,2}/\\d{1,2}/\\d{4}";
    public static final Deadline CURRENT_DATE =
            new Deadline(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
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

    public String getValue() {
        return value;
    }

    /**
     * Returns true if {@code test} is a valid transaction deadline.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidDeadline(String test) {
        return test.matches(TRANSACTION_DEADLINE_VALIDATION_REGEX) && checkDate(test, false);
    }

    /**
     * Returns true if {@code test} is a valid transaction deadline in the future.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidFutureDeadline(String test) {
        return test.matches(TRANSACTION_DEADLINE_VALIDATION_REGEX) && checkDate(test, true);
    }

    /**
     * Checks whether the string {@test} that follow date format is actually a valid date
     * according to the Gregorian Calendar.
     * @param test the command line argument to be parsed
     * @param checkFuture indicates whether to check if the date is in the future
     */
    private static boolean checkDate(String test, boolean checkFuture) {
        int dayOfMonth = Integer.parseInt(test.split("/")[0]);
        int month = Integer.parseInt(test.split("/")[1]);
        int year = Integer.parseInt(test.split("/")[2]);
        try {
            LocalDate date = LocalDate.of(year, month, dayOfMonth);
            return checkFuture ? !date.isBefore(LocalDate.now()) : true;
        } catch (DateTimeException ex) {
            LogsCenter.getLogger(Deadline.class).warning(ex.getMessage());
            return false;
        }

    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Deadline)) {
            return false;
        }
        Deadline deadline = (Deadline) other;
        return other == this || value.equals(deadline.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Converts a Deadline object to a LocalDateTime object.
     */
    private LocalDate convertToDate() {
        int dayOfMonth = Integer.parseInt(value.split("/")[0]);
        int month = Integer.parseInt(value.split("/")[1]);
        int year = Integer.parseInt(value.split("/")[2]);
        return LocalDate.of(year, month, dayOfMonth);
    }

    private long getDifference(Deadline other, String timeUnit) {
        LocalDate currentDate = this.convertToDate();
        LocalDate otherDate = other.convertToDate();
        long result;
        switch (timeUnit) {
        case "months":
            result = currentDate.until(otherDate, ChronoUnit.MONTHS);
            break;
        case "days":
            result = currentDate.until(otherDate, ChronoUnit.DAYS);
            break;
        case "years":
            result = currentDate.until(otherDate, ChronoUnit.YEARS);
            break;
        default:
            result = 0;
        }
        return result;
    }

    public long getMonthsDifference() {
        return Deadline.CURRENT_DATE.getMonthsDifference(this);
    }

    private long getMonthsDifference(Deadline other) {
        return getDifference(other, "months");
    }

    private long getDaysDifference(Deadline other) {
        return getDifference(other, "days");
    }

    private long getYearsDifference(Deadline other) {
        return getDifference(other, "years");
    }

    /**
     * Compares two deadlines in chronological order.
     * @param other the other deadline to compare.
     */
    public int compareTo(@NotNull Deadline other) {
        if (getYearsDifference(other) > 0 || getMonthsDifference(other) > 0 || getDaysDifference(other) > 0) {
            return -1;
        } else if (getYearsDifference(other) == 0 && getMonthsDifference(other) == 0 && getDaysDifference(other) == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
