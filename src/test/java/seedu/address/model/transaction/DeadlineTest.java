package seedu.address.model.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DeadlineTest {
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
        assertFalse(Deadline.isValidDeadline("24/09/2018")); //deadline has already passed

        //valid deadlines
        assertTrue(Deadline.isValidDeadline("25/12/2018"));
        assertTrue(Deadline.isValidDeadline("28/04/2019"));
    }
}
