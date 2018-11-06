package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;


/**
 * Lists all past transactions in the Debt Tracker to the user.
 */
public class PastCommand extends Command {
    public static final String COMMAND_WORD = "past";
    public static final String COMMAND_ALIAS = "pt";

    public static final String MESSAGE_SUCCESS = "Listed all paid transactions.";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPastTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        model.commitFinancialDatabase();
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
