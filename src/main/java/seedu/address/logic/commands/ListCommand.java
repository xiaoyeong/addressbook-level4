package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;


/**
 * Lists all persons in the Debt Tracker to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all transactions";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        model.updateFilteredPastTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
