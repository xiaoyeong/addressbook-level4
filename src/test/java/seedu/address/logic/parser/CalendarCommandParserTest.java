package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.commands.CalendarCommand;

public class CalendarCommandParserTest {

    private CalendarCommandParser parser = new CalendarCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CalendarCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsCalendarCommand() {
        // no leading and trailing whitespaces
        List<String> validArgs = Arrays.asList("show", "login", "logout", "sync");
        for (String arg : validArgs) {
            CalendarCommand expectedCalendarCommand =
                    new CalendarCommand(arg);
            assertParseSuccess(parser, " " + arg, expectedCalendarCommand);
        }
    }

}
