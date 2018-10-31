package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Transaction;

/**
 * Calculates an interest value for a given transaction (either using simple or compound scheme as specified by the
 * user).
 */
public class InterestCommand extends Command {
    public static final String COMMAND_WORD = "interest";
    public static final String COMMAND_ALIAS = "int";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Calculates interest on all transactions "
            + "based on the interest scheme and value that the user inputs.\n"
            + "Parameters: INTEREST_SCHEME INTEREST_RATE...\n"
            + "Example: " + COMMAND_WORD + " simple 1.1%";
    public static final String MESSAGE_SUCCESS = "Interest calculated for all %d transactions!";
    private final String scheme;
    private final String rate;


    public InterestCommand(String scheme, String rate) {
        this.scheme = scheme;
        this.rate = rate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        List<Transaction> lastShownList = model.getFilteredTransactionList();
        for (Transaction transactionToEdit : lastShownList) {
            Amount principalAmount = transactionToEdit.getAmount();
            long monthsDifference = transactionToEdit.getDeadline().getMonthsDifference();
            Amount convertedAmount = Amount.calculateInterest(principalAmount, scheme, rate, monthsDifference);
            Transaction editedTransaction = Transaction.copy(transactionToEdit);
            editedTransaction.setAmount(convertedAmount);
            model.updateTransaction(transactionToEdit, editedTransaction);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, lastShownList.size()));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof InterestCommand)) {
            return false;
        }
        InterestCommand command = (InterestCommand) other;
        return command == this
                || (scheme.equals(command.scheme)
                 && rate.equals(command.rate));
    }
}
