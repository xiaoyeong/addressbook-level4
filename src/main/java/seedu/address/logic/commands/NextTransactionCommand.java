package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.DeadlineContainsKeywordsPredicate;
import seedu.address.model.transaction.Transaction;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class NextTransactionCommand extends Command {

    public static final String COMMAND_WORD = "nextt";
    public static final String COMMAND_ALIAS = "nt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all transactions with the nearest"
            + "deadline contain any of \n"
            + "Example: " + COMMAND_WORD;
    private DeadlineContainsKeywordsPredicate predicate;

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        List<Transaction> transactionList = model.getFilteredTransactionList();
        Deadline firstDate = null;
        if (transactionList.size() > 0) {
            firstDate = transactionList.get(0).getDeadline();

            for (int i = 1; i < transactionList.size(); i++) {
                Transaction t = transactionList.get(i);
                if (firstDate.compareTo(t.getDeadline()) == 1) {
                    firstDate = t.getDeadline();
                }
            }
            predicate = new DeadlineContainsKeywordsPredicate(Arrays.asList(firstDate.toString()));
            model.updateFilteredTransactionList(predicate);
        }


        return new CommandResult(
                String.format(Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW,
                        model.getFilteredTransactionList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NextTransactionCommand // instanceof handles nulls
                && predicate.equals(((NextTransactionCommand) other).predicate)); // state check
    }
}
