package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents the amount of money loaned/owed to end user.
 * Guarantees: immutable; is valid as declared in {@link #isValidAmount(String)}
 */
public class Amount implements Comparable<Amount> {
    public static final Set<Currency> CURRENCIES = Currency.getAvailableCurrencies();
    public static final String MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS =
            "The transaction amount must be real number rounded to two decimal places, "
                    + "prefixed by a valid ISO 4217 three letter currency code";
    public static final String TRANSACTION_AMOUNT_VALIDATION_REGEX = "\\w{3} \\d+\\.\\d{2}";
    public static final Amount DEFAULT_AMOUNT = new Amount();
    private Currency currency;
    private String value;
    private InterestScheme interestScheme;
    private InterestRate interestRate;

    public Amount() {
        currency = Currency.getInstance("SGD");
        value = "0.00";
    }

    /**
     * Constructs an {@code TransactionAmount}.
     *
     * @param amount A valid transaction amount.
     */
    public Amount(String amount) {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS);
        currency = Currency.getInstance(amount.split("\\s+")[0]);
        value = amount.split("\\s+")[1];
    }

    public Double getValue() {
        return Double.parseDouble(value);
    }


    /**
     * Returns true if the given string represents a valid transaction amount.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidAmount(String test) {
        return test.matches(TRANSACTION_AMOUNT_VALIDATION_REGEX) && checkCurrency(test);
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

    /**
     * Calculates interest based on interestScheme and interestRate inputted by the user.
     */
    public static Amount calculateInterest(Amount principalAmount, InterestScheme interestScheme,
                                           InterestRate interestRate, long durationInMonths) {
        int monthsInAYear = 12;
        Amount convertedAmount = new Amount();
        convertedAmount.interestScheme = interestScheme;
        convertedAmount.interestRate = interestRate;
        double originalValue = principalAmount.getValue();
        double calculatedValue;
        if (convertedAmount.interestScheme.getValue().equals("simple")) {
            calculatedValue = originalValue
                    + originalValue * convertedAmount.interestRate.getValue() * durationInMonths;
            convertedAmount.value = String.format("%.2f", calculatedValue);
        } else {
            calculatedValue = originalValue * Math.pow(1.0 + convertedAmount.interestRate.getValue() / monthsInAYear,
                            durationInMonths);
            convertedAmount.value = String.format("%.2f", calculatedValue);
        }
        return convertedAmount;
    }

    /**
     * Handles the conversion of foreign currency to Singaporean currency.
     *
     * @param amount the amount in a given currency which is to be converted to Singaporean currency
     */
    @SuppressWarnings("unchecked")
    public static Amount convertCurrency(Amount amount) throws IOException {
        Currency currencyCode = amount.currency;
        if (currencyCode.toString().equals("SGD")) {
            return amount;
        }
        String currencyConverterFilePath = String.format(
                "http://free.currencyconverterapi.com/api/v5/convert?q=%s_SGD&compact=y", currencyCode);
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = new URL(currencyConverterFilePath).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = rd.readLine();
        Map<String, Map<String, Number>> map = mapper.readValue(jsonText, Map.class);
        double result = 1.00 * map.get(String.format("%s_SGD", currencyCode)).get("val").doubleValue();
        result *= amount.getValue();
        return new Amount(String.format("SGD %.2f", result));
    }

    @Override
    public String toString() {
        return currency + " " + value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Amount)) {
            return false;
        }
        Amount amount = (Amount) other;
        return other == this || (value.equals(amount.value) && currency.equals(amount.currency));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Amount otherAmount) {
        return getValue().compareTo(otherAmount.getValue());
    }
}
