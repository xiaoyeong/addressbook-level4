package seedu.address.model.transaction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Currency;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Set;
import seedu.address.commons.exceptions.DataConversionException;
import static seedu.address.commons.util.AppUtil.checkArgument;
import seedu.address.commons.util.JsonUtil;

/**
 * Represents the amount of money loaned/owed to end user.
 * Guarantees: immutable; is valid as declared in {@link #isValidAmount(String)}
 */
public class Amount {
    public static final Set<Currency> CURRENCIES = Currency.getAvailableCurrencies();
    public static final String MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS =
              "The transaction amount must be real number rounded to two decimal places, "
            + "prefixed by a three letter currency code";
    /*
     * The transaction amount must be in the following format:
     */
    public static final String TRANSACTION_AMOUNT_VALIDATION_REGEX = "\\w{3} \\d{1,}.\\d{2}";

    public final String value;
    private float val;


    /**
     * Constructs an {@code TransactionAmount}.
     *
     * @param amount A valid transaction amount.
     */
    public Amount(String amount) {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS);
        value = amount;
    }

    public float getVal() {
        return val;
    }

    /**
     * Returns true if a given string is a valid transaction amount.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidAmount(String test) {
        return test.matches(TRANSACTION_AMOUNT_VALIDATION_REGEX) && checkCurrency(test);
    }

    public static void convertCurrency(String amount) {
        String currencyCode = amount.split(" ")[0].toUpperCase();
        Path filePath = Paths.get(String.format(
                "http://free.currencyconverterapi.com/api/v5/convert?q=%s_SGD&compact=y", currencyCode));
        try {
            Optional<Amount> valueData = JsonUtil.readJsonFile(filePath, Amount.class);
            System.out.println(valueData);
        } catch (DataConversionException ex) {
            ex.printStackTrace();
        }

    }
    /**
     * Compares the currency code of the string {@code test} with the codes of the existing currencies.
     * @param test the command line argument to be parsed
     */
    private static boolean checkCurrency(String test) {
        String testCurrencyCode = test.split(" ")[0].toUpperCase();
        for (Currency currency: CURRENCIES) {
            if (currency.getCurrencyCode().equals(testCurrencyCode)) {
                return true;
            }
        }
        return false;
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
        Type Type = (Type) other;
        return other == this || value.equals(Type.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
