package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.InterestCommand;
import seedu.address.model.transaction.InterestRate;
import seedu.address.model.transaction.InterestScheme;

public class InterestCommandParserTest {
    private InterestCommandParser parser = new InterestCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterestCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsInterestCommand() {
        // no leading and trailing whitespaces
        InterestCommand expectedInterestCommand =
                new InterestCommand("compound", "3.40%");
        assertParseSuccess(parser, "compound 3.40%", expectedInterestCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n compound \n \t 3.40%  \t", expectedInterestCommand);
    }

    @Test
    public void parse_invalidScheme_throwsParseException() {
        assertParseFailure(parser, "sample 4.00%", InterestScheme.MESSAGE_INTEREST_SCHEME_CONSTRAINTS);
    }

    @Test
    public void parse_invalidRate_throwsParseException() {
        //more than 2 decimal places
        assertParseFailure(parser, "compound 4.635%", InterestRate.MESSAGE_INTEREST_RATE_CONSTRAINTS);
        //interest rate above 4%
        assertParseFailure(parser, "compound 4.63%", InterestRate.MESSAGE_INTEREST_RATE_CONSTRAINTS);
        //less than 2 decimal places
        assertParseFailure(parser, "compound 4.0%", InterestRate.MESSAGE_INTEREST_RATE_CONSTRAINTS);
    }

    @Test
    public void parse_missingOneArg_throwsParseException() {
        assertParseFailure(parser, "simple  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterestCommand.MESSAGE_USAGE));
    }

}
