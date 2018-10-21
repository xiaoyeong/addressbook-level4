package seedu.address.model.transaction;

/**
 * Represents the interest calculated on a principal sum in a given transaction
 * Guarantees: immutable; is valid as declared in {@link #isValidInterest(String)}
 */
public class Interest {
    public static final String MESSAGE_TRANSACTION_INTEREST_CONSTRAINTS =
            "The parameters for calculating interest are: <SCHEME> <INTEREST_RATE>"
                    + "where SCHEME is either simple or compound"
                    + "INTEREST_RATE is a percentage rounded to two decimal places";
    public static final String TRANSACTION_INTEREST_VALIDATION_REGEX = "(simple|compound)\\s\\d{1,2}.\\d{1,2}\\%";
    public final String value;

    public Interest(String value) {
        this.value = value;
    }

    /**
     * Returns true if the given string contains valid parameters for interest calculation.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidInterest(String test) {
        return test.toLowerCase().matches(TRANSACTION_INTEREST_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Interest)) {
            return false;
        }
        Interest interest = (Interest) other;
        return other == this || value.equals(interest.value);
    }

    @Override
    public String toString() {
        return value;
    }
}

