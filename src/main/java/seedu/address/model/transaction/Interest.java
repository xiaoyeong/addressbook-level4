package seedu.address.model.transaction;

/**
 * Represents the interest calculated on a principal sum in a given transaction
 */
public class Interest extends Amount {
    private static final int MONTHS_IN_A_YEAR = 12;
    private InterestScheme scheme;
    private InterestRate rate;

    public Interest(Amount principal, InterestScheme scheme, InterestRate rate, long durationInMonths) {
        this.rate = rate;
        this.scheme = scheme;
        currency = principal.getCurrency();

        if (scheme.getValue().equals("simple")) {
            value = Double.parseDouble(
                    String.format("%.2f", principal.getValue() * rate.getValue() * durationInMonths));
        } else {
            double sum = 0.0;
            double originalValue = principal.getValue();
            double calculatedValue =
                    originalValue * Math.pow(1.0 + rate.getValue() / MONTHS_IN_A_YEAR, durationInMonths);
            value = Double.parseDouble(
                    String.format("%.2f", calculatedValue - originalValue));
        }
    }
}

