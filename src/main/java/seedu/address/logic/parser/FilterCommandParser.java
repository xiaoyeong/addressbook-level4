package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FieldContainsKeywordsPredicate;





/**
 * Parses input arguments and creates a new AddCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!anyPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        List<FieldContainsKeywordsPredicate> predicates = new ArrayList<FieldContainsKeywordsPredicate>();
        /*
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String[] nameKeywords = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            predicates.add(new FieldContainsKeywordsPredicate(FieldType.Name, Arrays.asList(nameKeywords)));
        }*/
        addPredicates(argMultimap, predicates);

        return new FilterCommand(predicates);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean anyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     *
     * @param argumentMultimap
     * @param predicateList
     */
    private void addPredicates(ArgumentMultimap argumentMultimap, List<FieldContainsKeywordsPredicate> predicateList) {
        if (argumentMultimap.getValue(PREFIX_NAME).isPresent()) {
            String[] nameKeywords = argumentMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            predicateList.add(new FieldContainsKeywordsPredicate(FieldType.Name, Arrays.asList(nameKeywords)));
        }
        if (argumentMultimap.getValue(PREFIX_PHONE).isPresent()) {
            String[] phoneKeywords = argumentMultimap.getValue(PREFIX_PHONE).get().split("\\s+");
            predicateList.add(new FieldContainsKeywordsPredicate(FieldType.Phone, Arrays.asList(phoneKeywords)));
        }
        if (argumentMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            String[] emailKeywords = argumentMultimap.getValue(PREFIX_EMAIL).get().split("\\s+");
            predicateList.add(new FieldContainsKeywordsPredicate(FieldType.Email, Arrays.asList(emailKeywords)));
        }
        if (argumentMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String[] addressKeywords = argumentMultimap.getValue(PREFIX_ADDRESS).get().split("\\s+");
            predicateList.add(new FieldContainsKeywordsPredicate(FieldType.Address, Arrays.asList(addressKeywords)));
        }
    }


}
