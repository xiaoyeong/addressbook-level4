/* @@author xiaoyeong */
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;

/**
 * Analyse the your financial status
 */
public class AnalyticsCommand extends Command {
    public static final String COMMAND_WORD = "analytics";
    public static final String COMMAND_ALIAS = "an";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Analyse the your financial status and generate \n"
            + "your financial status to view.\n"
            + "It will either generate your financial status base on all the list,\n"
            + "or generate to a certain date, base on the date you input."
            + "eg analytics or analytics dd/mm/yyyy";

    public static final String MESSAGE_SUCCESS = "Financial status : SGD %.2f";

    private final Deadline deadline;

    public AnalyticsCommand() {
        this.deadline = null;
    }

    public AnalyticsCommand(Deadline deadline) {
        this.deadline = deadline;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        double totalSum;
        totalSum = 0.0;
        requireNonNull(model);
        List<Transaction> transactionList = model.getFilteredTransactionList();


        if (deadline != null && !Deadline.isValidDeadline(deadline.toString())) {
            throw new CommandException(Messages.MESSAGE_INVALID_DATE);
        }

        for (int i = 0; i < transactionList.size(); i++) {
            Transaction t = transactionList.get(i);
            if (deadline == null || deadline.compareTo(t.getDeadline()) == 1) {
                Amount currentAmount = Amount.convertCurrency(t.getAmount());
                if (currentAmount != null) {
                    if (t.getType().toString().compareTo("debt") == 0) {
                        totalSum -= currentAmount.getValue();
                    } else if ((t.getType().toString().compareTo("loan") == 0)) {
                        totalSum += currentAmount.getValue();
                    }
                }
            }
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, totalSum));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AnalyticsCommand)) {
            return false;
        }

        // state check
        AnalyticsCommand analyticsCommand = (AnalyticsCommand) other;
        return deadline.equals(analyticsCommand.deadline);
    }
}

