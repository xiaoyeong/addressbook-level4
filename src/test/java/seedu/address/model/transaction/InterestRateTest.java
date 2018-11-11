package seedu.address.model.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class InterestRateTest {
    @Test
    public void constructorTest() {
        Assert.assertThrows(NullPointerException.class, () -> new InterestRate(null));
    }

    @Test
    public void getValueTest() {
        InterestRate interestRate = new InterestRate("2.56%");
        double rate = interestRate.getValue();
        assertEquals(0.0256, rate, 0.002);
    }

    @Test
    public void isValidInterestRateTest() {
        //null interest rate
        Assert.assertThrows(NullPointerException.class, () -> InterestRate.isValidInterestRate(null));

        //invalid interest rates
        assertFalse(InterestRate.isValidInterestRate("1.53")); //interest rate not expressed as a percentage
        assertFalse(InterestRate.isValidInterestRate("1%")); //missing fractional part
        assertFalse(InterestRate.isValidInterestRate("4.01%")); //slightly above the capped interest rate
        assertFalse(InterestRate.isValidInterestRate("4.73%"));
        assertFalse(InterestRate.isValidInterestRate("125.35%")); //a very large interest rate
        assertFalse(InterestRate.isValidInterestRate("1.5%")); //less than 2 decimal places
        assertFalse(InterestRate.isValidInterestRate("1.534%")); //more than 2 decimal places
        assertFalse(InterestRate.isValidInterestRate("1.a8%")); //contains alphabetic character after decimal point
        assertFalse(InterestRate.isValidInterestRate("d.12%")); //contains alphabetic character before decimal point

        //valid interest rates
        assertTrue(InterestRate.isValidInterestRate("1.53%")); //a very large interest rate
        assertTrue(InterestRate.isValidInterestRate("4.00%")); //equal to the capped interest rate
        assertTrue(InterestRate.isValidInterestRate("3.99%")); //slightly lesser than the capped interest rate
    }

    @Test
    public void toStringTest() {
        String rate = "2.53%";
        InterestRate interestRate = new InterestRate(rate);
        assertEquals(interestRate.toString(), rate);
    }

    @Test
    public void equalsTest() {
        String rate = "3.46%";
        String amount = "JPY 55.60";
        InterestRate firstInterestRate = new InterestRate(rate);
        InterestRate secondInterestRate = new InterestRate(rate);
        Amount amountNotInterestRate = new Amount(amount);
        assertEquals(firstInterestRate, secondInterestRate);
        assertNotEquals(firstInterestRate, amountNotInterestRate);
    }

}
