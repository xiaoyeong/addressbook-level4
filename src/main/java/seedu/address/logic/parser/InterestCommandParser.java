package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.InterestCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.transaction.InterestRate;
import seedu.address.model.transaction.InterestScheme;

/**
 * Parses input arguments and creates a new InterestCommand object
 */
public class InterestCommandParser implements Parser<InterestCommand> {
    @Override
    public InterestCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterestCommand.MESSAGE_USAGE));
        }
        String[] nameKeywords = trimmedArgs.split("\\s+");
        if (nameKeywords.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterestCommand.MESSAGE_USAGE));
        }
        String scheme = nameKeywords[0];
        if (!InterestScheme.isValidInterestScheme(scheme)) {
            throw new ParseException(InterestScheme.MESSAGE_INTEREST_SCHEME_CONSTRAINTS);
        }
        String rate = nameKeywords[1];
        if (!InterestRate.isValidInterestRate(rate)) {
            throw new ParseException(InterestRate.MESSAGE_INTEREST_RATE_CONSTRAINTS);
        }
        return new InterestCommand(scheme, rate);
    }
}
