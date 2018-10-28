package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ToDoListAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 *
 */

public class ToDoListAddCommandParser implements Parser<ToDoListAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ToDoListAddCommand
     * and returns an ToDoListAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public ToDoListAddCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ToDoListAddCommand.MESSAGE_USAGE));
        }

        return new ToDoListAddCommand(args);
    }
}

