package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PREFIX_COMBINATION;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PREFIX_VALUE;
import static seedu.address.commons.core.Messages.MESSAGE_KEYWORDS_NONEMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OR;
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

    @Test
    public void multipleIdenticalPrefixes_throwsParseException(){
        assertParseFailure(parser, " e/example.com e/gmail.com", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void prefixWithEmptyKeywords_throwsParseException(){
        assertParseFailure(parser, " n/", MESSAGE_KEYWORDS_NONEMPTY);
        assertParseFailure(parser, " e/;;", MESSAGE_KEYWORDS_NONEMPTY);
    }

    @Test
    public void parse_operatorPrefixOnly_throwsParseException(){
        assertParseFailure(parser, " or/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_twoOperators_throwsParseException(){
        assertParseFailure(parser, " n/alice e/example.com or/ and/",
                String.format(MESSAGE_INVALID_PREFIX_COMBINATION, PREFIX_OR, PREFIX_AND));
    }

    @Test
    public void parse_orOperatorHasValue_throwsParseException(){
        assertParseFailure(parser, " n/alice e/example.com or/value",
                String.format(MESSAGE_INVALID_PREFIX_VALUE, PREFIX_OR));
    }

    @Test
    public void parse_andOperatorHasValue_throwsParseException(){
        assertParseFailure(parser, " n/alice e/example.com and/value",
                String.format(MESSAGE_INVALID_PREFIX_VALUE, PREFIX_AND));
    }


}
