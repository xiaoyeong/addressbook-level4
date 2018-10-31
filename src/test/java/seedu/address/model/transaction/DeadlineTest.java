package seedu.address.model.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        // null deadline
        Assert.assertThrows(NullPointerException.class, () -> Deadline.isValidDeadline(null));

        //invalid deadlines
        assertFalse(Deadline.isValidDeadline("")); //empty string
        assertFalse(Deadline.isValidDeadline(" ")); //spaces only
        assertFalse(Deadline.isValidDeadline("30/02/2019")); //The incorrect day of the month
        assertFalse(Deadline.isValidDeadline("152/12/2019")); //incorrect day
        assertFalse(Deadline.isValidDeadline("12/13/2018")); //incorrect month
        assertFalse(Deadline.isValidDeadline("31/11/201")); //incorrect year
        assertFalse(Deadline.isValidFutureDeadline("24/09/2018")); //deadline has already passed

        //valid deadlines
        assertTrue(Deadline.isValidDeadline("25/12/2018"));
        assertTrue(Deadline.isValidDeadline("28/04/2019"));
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
        String date = String.format("%d/%d/%d", dayOfMonth, month, year);
        if (!Deadline.isValidDeadline(date)) {
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
        if (!Deadline.isValidDeadline(firstDate) || !Deadline.isValidDeadline(secondDate)) {
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
    public void compareTo_differentDeadlines_returnPositiveOne() {
        String[] dates = generateTwoDates();
        Deadline firstDeadline = new Deadline(dates[0]);
        Deadline secondDeadline = new Deadline(dates[1]);
        assertEquals(secondDeadline.compareTo(firstDeadline), 1);
    }

    @Test
    public void compareTo_differentDeadlines_returnNegativeOne() {
        String firstDate = "17/11/2018";
        String secondDate = "15/12/2018";
        Deadline firstDeadline = new Deadline(firstDate);
        Deadline secondDeadline = new Deadline(secondDate);
        assertEquals(firstDeadline.compareTo(secondDeadline), -1);
    }
}
