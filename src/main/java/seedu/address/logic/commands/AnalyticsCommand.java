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
            + "your financial status to view.\n";

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
        double amount;
        amount = 0.0;
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
                        amount += Double.parseDouble(currentAmount.getValue().split(" ")[1]);
                    } else if ((t.getType().toString().compareTo("loan") == 0)) {
                        amount -= Double.parseDouble(currentAmount.getValue().split(" ")[1]);
                    }
                }
            }
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, amount));
    }
}

