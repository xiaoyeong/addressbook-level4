package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Transaction;

/**
 * Analyse the your financial status
 */
public class AnalyticsCommand extends Command {
    public static final String COMMAND_WORD = "Analytics";
    public static final String COMMAND_ALIAS = "an";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Analyse the your financial status and generate \n"
            + "your expected income for the following week.\n";

    public static final String MESSAGE_SUCCESS = "Financial status : $ ";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Transaction> transactionList = model.getFilteredTransactionList();
        Amount[] amounts = new Amount[transactionList.size()];

        for (int i = 0; i < amounts.length; i++) {
            amounts[i] = transactionList.get(i).getAmount();
        }


        return new CommandResult(String.format(MESSAGE_SUCCESS, amounts));
    }
}

