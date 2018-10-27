package seedu.address.model.transaction;

import seedu.address.logic.commands.InterestRate;

/**
 * Represents the interest calculated on a principal sum in a given transaction
 */
public class Interest extends Amount {
    public Interest(Amount principal, InterestRate rate, InterestScheme scheme, long durationInMonths) {
        if (scheme == InterestScheme.Simple) {
            currency = principal.getCurrency();
            value = principal.getValue() * rate.getValue() * durationInMonths;
        } else {
            currency = principal.getCurrency();
            double sum = 0.0, incrementedValue = principal.getValue();
            for (long i = 0; i < durationInMonths; i++) {
                sum += incrementedValue * rate.getValue();
                incrementedValue += sum;
            }
            value = sum;
        }
    }
}

