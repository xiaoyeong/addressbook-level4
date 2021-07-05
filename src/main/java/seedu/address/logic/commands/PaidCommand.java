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
 * Removes a current transaction from the current transaction list, and adds it to the list of paid/past transactions.
 */
public class PaidCommand extends Command {
    public static final String COMMAND_WORD = "paid";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a transaction identified by the index number used in the current transaction list"
            + " and shifts it to the past transactions list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_PAID_TRANSACTION_SUCCESS = "Transaction paid!";
    public static final int DEFAULT_INDEX = 1;

    private final Index targetIndex;

    public PaidCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Transaction> lastShownList = model.getFilteredTransactionList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Transaction paidTransaction = lastShownList.get(targetIndex.getZeroBased());
        model.deleteTransaction(paidTransaction);
        model.addPastTransaction(paidTransaction);
        model.commitFinancialDatabase();
        return new CommandResult(String.format(MESSAGE_PAID_TRANSACTION_SUCCESS, paidTransaction));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PaidCommand // instanceof handles nulls
                && targetIndex.equals(((PaidCommand) other).targetIndex)); // state check
    }
}
