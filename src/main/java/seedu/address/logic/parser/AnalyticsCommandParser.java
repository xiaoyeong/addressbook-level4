/* @@author xiaoyeong */
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AnalyticsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.transaction.Deadline;

/**
 * Parses input arguments and creates a new AnalyticsCommand object
 */
public class AnalyticsCommandParser implements Parser<AnalyticsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AnalyticsCommand
     * and returns an AnalyticsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AnalyticsCommand parse(String args) throws ParseException {
        try {
            if (args != null && args.equals("")) {
                return new AnalyticsCommand();
            }
            Deadline deadline = ParserUtil.parseDeadline(args);
            return new AnalyticsCommand(deadline);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AnalyticsCommand.MESSAGE_USAGE), pe);
        }
    }

}
