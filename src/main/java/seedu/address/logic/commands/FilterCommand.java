package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
            + "Use the or/ prefix to match any or the and/ prefix to match all of the specified conditions"
            + ".\nParameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Examples: \n"
            + COMMAND_WORD + " n/alex e/example.com p/87438807 a/Geylang tt/debt\n"
            + COMMAND_WORD + " n/bernice tt/loan tdmax/11/11/2019\n"
            + COMMAND_WORD + " n/charlotte tamin/SGD 10.00 tamax/SGD 400.00\n"
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
