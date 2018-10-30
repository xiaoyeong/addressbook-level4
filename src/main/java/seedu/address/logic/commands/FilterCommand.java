package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT_MAX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT_MIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE_EARLIEST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE_LATEST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_TYPE;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.transaction.MultiFieldPredicate;
import seedu.address.model.transaction.Transaction;

/**
 * Finds and lists all persons in address book whose name/phone/email/address contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";
    public static final String COMMAND_ALIAS = "fi";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters all transactions whose specified field(s) "
            + "contain any of the specified keywords (case-insensitive) and displays them as a list.\n"
            + "Use the or/ prefix to match any or the and/ prefix to match all of the specified conditions\n"
            + "Use a semi-colon to separate multiple keywords, e.g. alex;bernice;charlotte"
            + "\nParameters: "
            + PREFIX_TRANSACTION_TYPE + "TRANSACTION_TYPE "
            + PREFIX_TRANSACTION_AMOUNT + "TRANSACTION_AMOUNT "
            + PREFIX_TRANSACTION_DEADLINE + "TRANSACTION_DEADLINE "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_TRANSACTION_AMOUNT_MIN + "MINIMUM_TRANSACTION_AMOUNT "
            + PREFIX_TRANSACTION_AMOUNT_MAX + "MAXIMUM_TRANSACTION_AMOUNT "
            + PREFIX_TRANSACTION_DEADLINE_EARLIEST + "EARLIEST_TRANSACTION_DEADLINE "
            + PREFIX_TRANSACTION_DEADLINE_LATEST + "LATEST_TRANSACTION_DEADLINE\n"
            + "Examples: \n"
            + COMMAND_WORD + " n/alex e/example.com p/87438807 a/Geylang tt/debt\n"
            + COMMAND_WORD + " n/alex;bernice;charlotte\n"
            + COMMAND_WORD + " tamin/SGD 10.00 tamax/SGD 400.00\n"
            + COMMAND_WORD + " tdmin/10/10/2018 tdmax/10/10/2019\n"
            + COMMAND_WORD + " n/charlotte e/alex or/\n";

    private final List<Predicate<Transaction>> predicates;
    private final MultiFieldPredicate.OperatorType operatorType;

    public FilterCommand(List<Predicate<Transaction>> predicates,
                         MultiFieldPredicate.OperatorType operatorType) {
        this.predicates = predicates;
        this.operatorType = operatorType;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredTransactionList(new MultiFieldPredicate(predicates, operatorType));
        return new CommandResult(
                String.format(Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW,
                        model.getFilteredTransactionList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FilterCommand // instanceof handles nulls
                && predicates.equals(((FilterCommand) other).predicates)
                && operatorType.equals(((FilterCommand) other).operatorType)
                ); // state check
    }
}
