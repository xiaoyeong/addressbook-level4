package seedu.address.model.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class AmountTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Amount(null));
    }

    @Test
    public void constructor_invalidAmount_throwsIllegalArgumentException() {
        String invalidAmount = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Amount(invalidAmount));
    }

    @Test
    public void equals() {
        Amount sampleAmount = new Amount("SGD 899.00");
        assertEquals(sampleAmount, sampleAmount);

        Amount sampleAmountWithChangedCurrency = new Amount("JPY 899.00");
        assertNotEquals(sampleAmount, sampleAmountWithChangedCurrency);

        Amount sampleAmountWithChangedValue = new Amount("SGD 900.00");
        assertNotEquals(sampleAmount, sampleAmountWithChangedValue);

        //incorrect type
        assertNotEquals(sampleAmount, "SGD 899.00");
    }

    @Test
    public void convertCurrency() throws IOException {
        Amount amountInSgd = new Amount("SGD 456.60");
        assertEquals(Amount.convertCurrency(amountInSgd), amountInSgd);

        Amount amountInUsd = new Amount("USD 331.30");
        assertEquals(amountInSgd.getValue(), Amount.convertCurrency(amountInUsd).getValue(), 0.1);
    }

    @Test
    public void calculateInterest() {
        Amount principalAmount = new Amount("SGD 55.20");
        Amount newAmountAfterSimple =
                Amount.calculateInterest(principalAmount, new InterestScheme("Simple"), new InterestRate("3.45%"), 5);
        Amount newAmountAfterCompound =
                Amount.calculateInterest(principalAmount, new InterestScheme("Compound"), new InterestRate("2.11%"), 8);
        assertEquals(newAmountAfterSimple, new Amount("SGD 64.72"));
        assertEquals(newAmountAfterCompound, new Amount("SGD 55.98"));

    }

    @Test
    public void compareAmounts() {
        String amountString = "AUD 59.75";
        Amount sampleAmount = new Amount(amountString);
        assertEquals(sampleAmount.toString(), amountString);

        Amount greaterAmount = new Amount("AUD 65.80");
        assertEquals(sampleAmount.compareTo(greaterAmount), -1);
        Amount lesserAmount = new Amount("AUD 42.40");
        assertEquals(sampleAmount.compareTo(lesserAmount), 1);

        assertEquals(sampleAmount.compareTo(sampleAmount), 0);
    }

    @Test
    public void isCorrectAmount() {
        // null amount
        Assert.assertThrows(NullPointerException.class, () -> Amount.isValidAmount(null));

        //invalid amounts
        assertFalse(Amount.isValidAmount("")); //empty string
        assertFalse(Amount.isValidAmount(" ")); //spaces only
        assertFalse(Amount.isValidAmount("$ 35.20")); //Currency symbol not allowed
        assertFalse(Amount.isValidAmount("SGD 45,60")); //special symbol `,` not allowed
        assertFalse(Amount.isValidAmount("37.45")); //does not have currency code as prefix
        assertFalse(Amount.isValidAmount("SGD132.50")); //does not have space between currency code and amount
        assertFalse(Amount.isValidAmount("EURA 156.75")); //has 4 letter currency code
        assertFalse(Amount.isValidAmount("MYR 165.580")); //the amount is not rounded to two decimal digits

        //valid amounts
        assertTrue(Amount.isValidAmount("SGD 227.50"));
        assertTrue(Amount.isValidAmount("USD 25987465246.50")); //large payment sum
    }
}
