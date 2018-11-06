package seedu.address.model.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void isCorrectAmount() {
        // null amount
        Assert.assertThrows(NullPointerException.class, () -> Amount.isValidAmount(null));

        //invalid amounts
        assertFalse(Amount.isValidAmount("")); //empty string
        assertFalse(Amount.isValidAmount(" ")); //spaces only
        assertFalse(Amount.isValidAmount("$ 35.20")); //Currency symbol not allowed
        assertFalse(Amount.isValidAmount("37.45")); //does not have currency code as prefix
        assertFalse(Amount.isValidAmount("SGD132.50")); //does not have space between currency code and amount
        assertFalse(Amount.isValidAmount("EURA 156.75")); //has 4 letter currency code
        assertFalse(Amount.isValidAmount("MYR 165.580")); //the amount is not rounded to two decimal digits

        //valid amounts
        assertTrue(Amount.isValidAmount("SGD 227.50"));
        assertTrue(Amount.isValidAmount("USD 25987465246.50")); //large payment sum
    }
}
