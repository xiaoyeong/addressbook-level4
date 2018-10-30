package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PREFIX_COMBINATION;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PREFIX_VALUE;
import static seedu.address.commons.core.Messages.MESSAGE_KEYWORDS_NONEMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT_MAX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT_MIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE_EARLIEST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE_LATEST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_TYPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.AmountBoundsPredicate;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.DeadlineBoundsPredicate;
import seedu.address.model.transaction.FieldContainsKeywordsPredicate;
import seedu.address.model.transaction.MultiFieldPredicate;
import seedu.address.model.transaction.Transaction;

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
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_TRANSACTION_AMOUNT, PREFIX_TRANSACTION_DEADLINE, PREFIX_TRANSACTION_TYPE,
                        PREFIX_OR, PREFIX_AND, PREFIX_TRANSACTION_AMOUNT_MAX, PREFIX_TRANSACTION_AMOUNT_MIN,
                        PREFIX_TRANSACTION_DEADLINE_EARLIEST, PREFIX_TRANSACTION_DEADLINE_LATEST,
                        PREFIX_TAG);

        if (!anyPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_TRANSACTION_AMOUNT, PREFIX_TRANSACTION_DEADLINE, PREFIX_TRANSACTION_TYPE,
                PREFIX_TRANSACTION_AMOUNT_MAX, PREFIX_TRANSACTION_AMOUNT_MIN, PREFIX_TRANSACTION_DEADLINE_EARLIEST,
                PREFIX_TRANSACTION_DEADLINE_LATEST)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        if (anyPrefixesWithMultipleInstances(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_TRANSACTION_AMOUNT, PREFIX_TRANSACTION_DEADLINE, PREFIX_TRANSACTION_TYPE, PREFIX_OR, PREFIX_AND,
                PREFIX_TRANSACTION_AMOUNT_MAX, PREFIX_TRANSACTION_AMOUNT_MIN,
                PREFIX_TRANSACTION_DEADLINE_EARLIEST, PREFIX_TRANSACTION_DEADLINE_LATEST)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        List<Predicate<Transaction>> predicates = new ArrayList<>();
        /*
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            String[] nameKeywords = argMultimap.getValue(PREFIX_NAME).get().split("\\s+");
            predicates.add(new FieldContainsKeywordsPredicate(FieldType.Name, Arrays.asList(nameKeywords)));
        }*/
        addPredicates(argMultimap, predicates);

        return new FilterCommand(predicates, getOperatorType(argMultimap));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean anyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private static boolean anyPrefixesWithMultipleInstances(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.prefixSize(prefix) > 1);
    }

    private static boolean anyStringEmpty(String[] stringList) {
        return Stream.of(stringList).anyMatch(s -> s.isEmpty());
    }


    private Map<Prefix, FieldType> getFieldTypeMap() {
        Map<Prefix, FieldType> typeMap = new HashMap<>();
        typeMap.put(PREFIX_NAME, FieldType.Name);
        typeMap.put(PREFIX_ADDRESS, FieldType.Address);
        typeMap.put(PREFIX_PHONE, FieldType.Phone);
        typeMap.put(PREFIX_EMAIL, FieldType.Email);
        typeMap.put(PREFIX_TRANSACTION_AMOUNT, FieldType.Amount);
        typeMap.put(PREFIX_TRANSACTION_DEADLINE, FieldType.Deadline);
        typeMap.put(PREFIX_TRANSACTION_TYPE, FieldType.Type);
        return typeMap;
    }

    /**
     *
     * @param argumentMultimap
     * @param predicateList
     */
    private void addPredicates(ArgumentMultimap argumentMultimap, List<Predicate<Transaction>> predicateList)
            throws ParseException {

        Map<Prefix, FieldType> typeMap = getFieldTypeMap();

        for (Map.Entry<Prefix, FieldType> entry : typeMap.entrySet()) {
            if (argumentMultimap.getValue(entry.getKey()).isPresent()) {
                String[] keywords = argumentMultimap.getValue(entry.getKey()).get().split(";" , -1);
                if (anyStringEmpty(keywords)) {
                    throw new ParseException(MESSAGE_KEYWORDS_NONEMPTY);
                }
                predicateList.add(new FieldContainsKeywordsPredicate(entry.getValue(), Arrays.asList(keywords)));
            }
        }

        if (argumentMultimap.getValue(PREFIX_TRANSACTION_DEADLINE_EARLIEST).isPresent()) {
            Deadline deadline = ParserUtil.parseDeadlineIgnoreFuture(argumentMultimap
                    .getValue(PREFIX_TRANSACTION_DEADLINE_EARLIEST).get());
            predicateList.add(new DeadlineBoundsPredicate(deadline, DeadlineBoundsPredicate.BoundType.EARLIEST));
        }

        if (argumentMultimap.getValue(PREFIX_TRANSACTION_DEADLINE_LATEST).isPresent()) {
            Deadline deadline = ParserUtil.parseDeadlineIgnoreFuture(argumentMultimap
                    .getValue(PREFIX_TRANSACTION_DEADLINE_LATEST).get());
            predicateList.add(new DeadlineBoundsPredicate(deadline, DeadlineBoundsPredicate.BoundType.LATEST));
        }

        if (argumentMultimap.getValue(PREFIX_TRANSACTION_AMOUNT_MAX).isPresent()) {
            Amount amount = ParserUtil.parseAmount(argumentMultimap.getValue(PREFIX_TRANSACTION_AMOUNT_MAX).get());
            Amount amountSgd = Amount.convertCurrency(amount);
            predicateList.add(new AmountBoundsPredicate(amountSgd, AmountBoundsPredicate.BoundType.MAX));
        }

        if (argumentMultimap.getValue(PREFIX_TRANSACTION_AMOUNT_MIN).isPresent()) {
            Amount amount = ParserUtil.parseAmount(argumentMultimap.getValue(PREFIX_TRANSACTION_AMOUNT_MIN).get());
            Amount amountSgd = Amount.convertCurrency(amount);
            predicateList.add(new AmountBoundsPredicate(amountSgd, AmountBoundsPredicate.BoundType.MIN));
        }

    }

    private MultiFieldPredicate.OperatorType getOperatorType(ArgumentMultimap argumentMultimap) throws ParseException {

        if (argumentMultimap.getValue(PREFIX_AND).isPresent() && argumentMultimap.getValue(PREFIX_OR).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PREFIX_COMBINATION, PREFIX_OR, PREFIX_AND));
        }

        if (argumentMultimap.getValue(PREFIX_OR).isPresent()) {
            if (!argumentMultimap.getValue(PREFIX_OR).get().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_PREFIX_VALUE, PREFIX_OR));
            }
            return MultiFieldPredicate.OperatorType.OR;
        }

        if (argumentMultimap.getValue(PREFIX_AND).isPresent()) {
            if (!argumentMultimap.getValue(PREFIX_AND).get().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_PREFIX_VALUE, PREFIX_AND));
            }
        }

        return MultiFieldPredicate.OperatorType.AND;
    }


}
