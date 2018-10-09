package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSONID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_TYPE;

import java.util.stream.Stream;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.commands.AddTransactionCommand;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.PersonId;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddTransactionCommandParser implements Parser<AddTransactionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTransactionCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PERSONID, PREFIX_TRANSACTION_AMOUNT, PREFIX_TRANSACTION_TYPE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_PERSONID, PREFIX_TRANSACTION_AMOUNT, PREFIX_TRANSACTION_TYPE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTransactionCommand.MESSAGE_USAGE));
        }

        PersonId personid = ParserUtil.parsePersonId(argMultimap.getValue(PREFIX_PERSONID).get());
        Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_TRANSACTION_AMOUNT).get());
        Type type = ParserUtil.parseType(argMultimap.getValue(PREFIX_TRANSACTION_TYPE).get());
        Deadline deadline = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_TRANSACTION_DEADLINE).get());

        Transaction transaction = new Transaction(type, amount, personid, deadline);

        return new AddTransactionCommand(transaction);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
