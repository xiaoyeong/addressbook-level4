/* @@author xiaoyeong*/
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

/**
 * Analyse the your financial status
 */
public class AdvanceAnalyticsCommand extends Command {
    public static final String COMMAND_WORD = "Advance";
    public static final String COMMAND_ALIAS = "adv";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Analyse the your financial status and generate \n"
            + "your expected loan/debt for the amount of months entered.\n";

    public static final String MESSAGE_SUCCESS = "Financial status : $ ";



    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        int amount;
        amount = 0;
        requireNonNull(model);
        List<Transaction> transactionList = model.getFilteredTransactionList();
        boolean loan = true;
        boolean debt = true;
        int deadline  = command.getDeadline();
        if(command =debt) {
            loan = false;
        }
        if(command = loan) {
            debt = false;
        }


        for (int i = 0; i < transactionList.size(); i++) {
            Transaction t = transactionList.get(i);

            if (debt && t.getType().toString().compareTo("Debt") == 0 && t.getDeadline() < deadline {
                amount += t.getAmount().getVal();
            } else if (loan && t.getType().toString().compareTo("Loan") == 0 && && t.getDeadline() < deadline) {
                amount -= t.getAmount().getVal();
            }
        }


        return new CommandResult(String.format(MESSAGE_SUCCESS, amount));
    }
}

