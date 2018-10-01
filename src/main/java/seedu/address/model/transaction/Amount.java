package seedu.address.model.transaction;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the amount of money loaned/owed to end user.
 * Guarantees: immutable; is valid as declared in {@link #isValidAmount(String)}
 */
public class Amount {
    public static final String MESSAGE_TRANSACTION_AMOUNT_CONSTRAINTS =
              "The transaction amount must be real number rounded to two decimal places, "
            + "prefixed by a three letter currency code";
    /*
     * The transaction amount must be in the following format:
     */
    public static final String TRANSACTION_AMOUNT_VALIDATION_REGEX = "\\w{3} \\d{1,}.\\d{2}";

    public final String value;

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


    /**
     * Returns true if a given string is a valid transaction amount.
     *
     * @param test the command line argument to be parsed
     */
    public static boolean isValidAmount(String test) {
        String lower = test.toLowerCase();
        return lower.equals("loan") || lower.equals("debt");
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
