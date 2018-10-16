package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import seedu.address.logic.commands.WildcardSearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.transaction.NameContainsLettersPredicate;

/**
 *
 */

public class WildcardSearchCommandParser implements Parser<WildcardSearchCommand>{

    /**
     * Parses the given {@code String} of arguments in the context of the WildcardSearchCommand
     * and returns an WildcardSearchCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public WildcardSearchCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, WildcardSearchCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new WildcardSearchCommand(new NameContainsLettersPredicate(Arrays.asList(nameKeywords)));
    }
}
