package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;




/**
 * Selects a transaction identified using it's displayed index from the address book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the transaction identified by the index number used in either the current or past "
            + " transaction list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "OR: " + COMMAND_WORD + " past " + "1";

    public static final String MESSAGE_SELECT_TRANSACTION_SUCCESS = "Selected Person: %1$s";

    private final String whichList;

    private final Index targetIndex;

    public SelectCommand(String whichList, Index targetIndex) {
        this.whichList = whichList;
        this.targetIndex = targetIndex;
    }
    public SelectCommand(Index targetIndex) {
        this.whichList = "";
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Transaction> filteredPersonList;
        if ("past".equals(whichList)) {
            filteredPersonList = model.getFilteredPastTransactionList();

            if (targetIndex.getZeroBased() >= filteredPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            EventsCenter.getInstance().post(new JumpToListRequestEvent(whichList, targetIndex));
        } else {
            filteredPersonList = model.getFilteredTransactionList();

            if (targetIndex.getZeroBased() >= filteredPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        }

        //model.commitFinancialDatabase();
        return new CommandResult(String.format(MESSAGE_SELECT_TRANSACTION_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
