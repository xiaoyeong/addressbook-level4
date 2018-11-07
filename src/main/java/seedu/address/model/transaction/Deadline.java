package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.sun.istack.NotNull;

/**
 * Represents the deadline by which payment has to be made.
 * Guarantees: the deadline is valid present/future date in the Gregorian calendar.
 */
public class Deadline implements Comparable<Deadline> {
    public static final String MESSAGE_TRANSACTION_DEADLINE_INCORRECT_FORMAT =
            "The transaction deadline must follow the DD/MM/YYYY format";
    public static final String MESSAGE_TRANSACTION_DEADLINE_HAS_PASSED =
            "The transaction deadline must be a date in the present or the future";
    public static final String TRANSACTION_DEADLINE_VALIDATION_REGEX = "\\d{1,2}/\\d{1,2}/\\d{4}";
    public static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
    public final String value;

    /**
     * Constructs an {@code Deadline}.
     */
    public Deadline(String deadline) {
        requireNonNull(deadline);
        checkArgument(matchesDateFormat(deadline), MESSAGE_TRANSACTION_DEADLINE_INCORRECT_FORMAT);
        value = deadline;
    }

    public String getValue() {
        return value;
    }

    /**
     * Returns true if {@code test} is a valid transaction deadline in the future.
     */
    public static boolean matchesDateFormat(String test) {
        return test.matches(TRANSACTION_DEADLINE_VALIDATION_REGEX);
    }

    /**
     * Checks whether the string {@test} represents a valid date according to the Gregorian Calendar .
     */
    public static void checkDateInFuture(String test) {
        LocalDate givenDate = convertToDate(test);
        if (givenDate.isBefore(convertToDate(Deadline.CURRENT_DATE))) {
            throw new DateTimeException(MESSAGE_TRANSACTION_DEADLINE_HAS_PASSED);
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
    private static LocalDate convertToDate(String test) {
        int dayOfMonth = Integer.parseInt(test.split("/")[0]);
        int month = Integer.parseInt(test.split("/")[1]);
        int year = Integer.parseInt(test.split("/")[2]);
        return LocalDate.of(year, month, dayOfMonth);
    }

    private long getDifference(Deadline other, String timeUnit) {
        LocalDate currentDate = convertToDate(value);
        LocalDate otherDate = convertToDate(other.value);
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
        Deadline todayDeadline = new Deadline(Deadline.CURRENT_DATE);
        return todayDeadline.getMonthsDifference(this);
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
