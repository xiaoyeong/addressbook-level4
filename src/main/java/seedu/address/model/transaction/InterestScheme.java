package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Contains constants for two schemes of interest calculation: simple and compound
 */
public class InterestScheme {
    /**
     * Represent two schemes for interest calculation.
     */
    public enum SchemeTypes {
        Simple("simple"), Compound("compound");

        private final String scheme;

        SchemeTypes(String scheme) {
            this.scheme = scheme;
        }

        public String getScheme() {
            return scheme;
        }
    }

    public static final String MESSAGE_INTEREST_SCHEME_CONSTRAINTS =
            "Interest scheme must be either simple or compound";
    public static final String TRANSACTION_INTEREST_SCHEME_VALIDATION_REGEX = "(simple|compound)";
    public final SchemeTypes value;

    public InterestScheme(String interestScheme) {
        requireNonNull(interestScheme);
        checkArgument(isValidInterestScheme(interestScheme), MESSAGE_INTEREST_SCHEME_CONSTRAINTS);
        if (interestScheme.equalsIgnoreCase("simple")) {
            this.value = SchemeTypes.Simple;
        } else {
            this.value = SchemeTypes.Compound;
        }
    }

    public String getValue() {
        return value.getScheme();
    }

    /**
     * Returns true if the given string contains valid parameters for interest calculation.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidInterestScheme(String test) {
        return test.toLowerCase().matches(TRANSACTION_INTEREST_SCHEME_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof InterestScheme)) {
            return false;
        }
        InterestScheme interestScheme = (InterestScheme) other;
        return other == this || (value == interestScheme.value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
