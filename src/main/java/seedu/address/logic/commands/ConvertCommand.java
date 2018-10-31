package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Transaction;

/**
 * Converts multiple amounts in different currencies to the base currency.
 */
public class ConvertCommand extends Command {
    public static final String COMMAND_WORD = "convert";
    public static final String COMMAND_ALIAS = "con";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Converts given amounts from their respective foreign currencies to Singapore Dollars\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Amounts converted in all transactions!!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        List<Transaction> lastShownList = model.getFilteredTransactionList();
        for (Transaction transactionToEdit : lastShownList) {
            Amount convertedAmount = Amount.convertCurrency(transactionToEdit.getAmount());
            Transaction editedTransaction = Transaction.copy(transactionToEdit);
            editedTransaction.setAmount(convertedAmount);
            model.updateTransaction(transactionToEdit, editedTransaction);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

