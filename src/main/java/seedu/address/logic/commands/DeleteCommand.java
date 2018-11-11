package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;


/**
 * Deletes a transaction identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the transaction identified by the index number used in either the current "
            + "or past transaction list. This transaction is not added anywhere.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "OR: " + COMMAND_WORD + " past " + "1";

    public static final String MESSAGE_DELETE_TRANSACTION_SUCCESS = "Deleted Transaction: %1$s";

    private final String whichList;

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.whichList = "";
        this.targetIndex = targetIndex;
    }

    public DeleteCommand(String whichList, Index targetIndex) {
        this.whichList = whichList;
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Transaction> lastShownList;
        if ("past".equals(whichList)) {
            lastShownList = model.getFilteredPastTransactionList();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Transaction transactionToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deletePastTransaction(transactionToDelete);
            model.commitFinancialDatabase();
            return new CommandResult(String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, transactionToDelete));
        } else {
            lastShownList = model.getFilteredTransactionList();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Transaction transactionToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deleteTransaction(transactionToDelete);
            model.commitFinancialDatabase();
            return new CommandResult(String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, transactionToDelete));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
