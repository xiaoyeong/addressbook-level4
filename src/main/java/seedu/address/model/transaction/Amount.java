package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.BufferedReader;
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
                    + "prefixed by a three letter currency code";
    public static final String TRANSACTION_AMOUNT_VALIDATION_REGEX = "\\w{3} \\d{1,}.\\d{1,2}";
    private static final int MONTHS_IN_A_YEAR = 12;
    private Currency currency;
    private Double value;
    private InterestScheme interestScheme;
    private InterestRate interestRate;

    public Amount() {
        currency = Currency.getInstance("SGD");
        value = 0.0;
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
        value = Double.parseDouble(amount.split("\\s+")[1]);
    }

    public double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
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
    public static Amount calculateInterest(Amount principalAmount, String interestScheme, String interestRate,
                                           long durationInMonths) {
        Amount convertedAmount = new Amount();
        convertedAmount.interestScheme = new InterestScheme(interestScheme);
        convertedAmount.interestRate = new InterestRate(interestRate);

        if (convertedAmount.interestScheme.getValue().equals("simple")) {
            convertedAmount.value = principalAmount.value + Double.parseDouble(String.format("%.2f",
                    principalAmount.value * convertedAmount.interestRate.getValue() * durationInMonths));
        } else {
            double originalValue = principalAmount.value;
            double calculatedValue =
                    originalValue * Math.pow(1.0 + convertedAmount.interestRate.getValue() / MONTHS_IN_A_YEAR,
                            durationInMonths);
            convertedAmount.value = principalAmount.value + Double.parseDouble(String.format("%.2f",
                    calculatedValue - originalValue));
        }
        return convertedAmount;
    }

    /**
     * Handles the conversion of foreign currency to Singaporean currency.
     *
     * @param amount the amount in a given currency which is to be converted to Singaporean currency
     */
    public static Amount convertCurrency(Amount amount) {
        if (!Amount.isValidAmount(amount.toString())) {
            return null;
        }
        Currency currencyCode = amount.currency;
        if (currencyCode.toString().equals("SGD")) {
            return amount;
        }
        String currencyConverterFilePath = String.format(
                "http://free.currencyconverterapi.com/api/v5/convert?q=%s_SGD&compact=y", currencyCode);
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream is = new URL(currencyConverterFilePath).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            Map<String, Map<String, Number>> map = mapper.readValue(jsonText, Map.class);
            double result = 1.00 * map.get(String.format("%s_SGD", currencyCode)).get("val").doubleValue();
            result *= amount.value;
            return new Amount(String.format("SGD %.2f", result));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the difference in value between two Amounts in Singaporean currency
     * @param amount1 The first amount to compare
     * @param amount2 The second amount to compare
     * @return The difference between the two Amounts as a double
     */
    public static double compareAmount (Amount amount1, Amount amount2) {
        Amount amountSgd1 = amount1.convertCurrency(amount1);
        Amount amountSgd2 = amount2.convertCurrency(amount2);
        return amountSgd1.value - amountSgd2.value;
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
        return other == this || value.equals(amount.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Amount otherAmount) {
        return value.compareTo(otherAmount.value);
    }
}
