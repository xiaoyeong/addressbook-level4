package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.transaction.FieldContainsKeywordsPredicate;
import seedu.address.model.transaction.MultiFieldPredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // no leading and trailing whitespaces
        FilterCommand expectedFilterCommand =
                new FilterCommand(Collections.singletonList(new FieldContainsKeywordsPredicate(FieldType.Name,
                        Arrays.asList("Alice", "Bob"))), MultiFieldPredicate.OperatorType.AND);
        assertParseSuccess(parser, " n/Alice;Bob", expectedFilterCommand);
    }

}
