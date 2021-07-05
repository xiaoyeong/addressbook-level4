package seedu.address.model.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.DateTimeException;
import java.util.Random;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DeadlineTest {
    private static final int POSITIVE_ONE = 1;
    private static final int NEGATIVE_ONE = -1;

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Deadline(null));
    }
    @Test
    public void constructor_invalidDeadline_throwsIllegalArgumentException() {
        String invalidDeadline = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Deadline(invalidDeadline));
    }
    @Test
    public void isCorrectDeadline() {
        /* null deadline */
        Assert.assertThrows(NullPointerException.class, () -> Deadline.matchesDateFormat(null));

        /* invalid deadlines */
        //empty string
        assertFalse(Deadline.matchesDateFormat(""));
        //spaces only
        assertFalse(Deadline.matchesDateFormat(" "));
        //incorrect day of the month
        Assert.assertThrows(DateTimeException.class, () -> Deadline.checkDateInFuture("30/02/2019"));
        //incorrect day
        Assert.assertThrows(DateTimeException.class, () -> Deadline.checkDateInFuture("152/12/2019"));
        //incorrect month
        Assert.assertThrows(DateTimeException.class, () -> Deadline.checkDateInFuture("12/13/2018"));
        //incorrect year
        Assert.assertThrows(DateTimeException.class, () -> Deadline.checkDateInFuture("31/11/201"));
        //deadline has already passed
        Assert.assertThrows(DateTimeException.class, () -> Deadline.checkDateInFuture("24/09/2018"));

        /* valid deadlines */
        assertTrue(Deadline.matchesDateFormat("25/12/2018"));
        assertTrue(Deadline.matchesDateFormat("28/04/2019"));
    }

    @Test
    public void equals() {
        Deadline sampleDeadline = new Deadline("24/07/2020");
        assertEquals(sampleDeadline, sampleDeadline);

        Deadline sampleDeadlineDateChanged = new Deadline("04/07/2020");
        assertNotEquals(sampleDeadline, sampleDeadlineDateChanged);

        assertNotEquals(sampleDeadline, "24/07/2020");
    }

    //TODO: improve this randomized testing
    /**
     * Generates a random date (date with random valid values for day, month and year)
     */
    private String generateOneDate() {
        Random rng = new Random();
        String dateFormat = "%d/%d/%d";
        int dayOfMonth = rng.nextInt(31) + 1; //day range is from 1 - 31
        int month = rng.nextInt(12) + 1; //month range is from 1 - 12
        int year = rng.nextInt(21) + 2018; //testing for uptil 20 years from baseline year 2018
        String date = String.format(dateFormat, dayOfMonth, month, year);
        if (!Deadline.matchesDateFormat(date)) {
            return generateOneDate();
        }
        try {
            Deadline.checkDateInFuture(date);
        } catch (DateTimeException ex) {
            return generateOneDate();
        }
        return date;
    }

    /**
     * Generates two random dates with one date occurring chronologically before the other.
     */
    private String[] generateTwoDates() {
        Random rng = new Random();
        String dateFormat = "%d/%d/%d";
        int firstDayOfMonth = rng.nextInt(15) + 1; //day range for first date is from 1 - 15
        int secondDayOfMonth = rng.nextInt(16) + 16; //day range for second date is from 16 - 31
        int firstMonth = rng.nextInt(6) + 1; //month range for first month is from 1 - 6
        int secondMonth = rng.nextInt(6) + 7; //month range for second month is from 7 - 12
        int firstYear = rng.nextInt(10) + 2018; //year range is from 2018 - 2027
        int secondYear = rng.nextInt(11) + 2028; //year range is from 2028 - 2038
        String firstDate = String.format(dateFormat, firstDayOfMonth, firstMonth, firstYear);
        String secondDate = String.format(dateFormat, secondDayOfMonth, secondMonth, secondYear);
        if (!Deadline.matchesDateFormat(firstDate) || !Deadline.matchesDateFormat(secondDate)) {
            return generateTwoDates();
        }
        try {
            Deadline.checkDateInFuture(firstDate);
            Deadline.checkDateInFuture(secondDate);
        } catch (DateTimeException ex) {
            return generateTwoDates();
        }
        return new String[]{firstDate, secondDate};
    }

    @Test
    public void compareTo_sameDeadlines_returnZero() {
        String date = generateOneDate();
        Deadline firstDeadline = new Deadline(date);
        Deadline secondDeadline = new Deadline(date);
        assertEquals(firstDeadline.compareTo(secondDeadline), 0);
    }

    @Test
    public void compareTo_differentDeadlines_returnNegativeOne() {
        String[] dates = generateTwoDates();
        Deadline firstDeadline = new Deadline(dates[0]);
        Deadline secondDeadline = new Deadline(dates[1]);
        assertEquals(firstDeadline.compareTo(secondDeadline), -1);
    }

    @Test
    public void compareTo_differentDeadlines_returnPositiveOne() {
        String[] dates = generateTwoDates();
        Deadline firstDeadline = new Deadline(dates[1]);
        Deadline secondDeadline = new Deadline(dates[0]);
        assertEquals(firstDeadline.compareTo(secondDeadline), 1);
    }
}
