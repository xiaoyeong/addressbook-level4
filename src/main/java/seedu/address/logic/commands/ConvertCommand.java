package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.transaction.Amount;

/**
 * Converts multiple amounts in different currencies to the base currency.
 */
public class ConvertCommand extends Command {
    public static final String COMMAND_WORD = "convert";
    public static final String COMMAND_ALIAS = "con";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Converts given amounts from their respective foreign currencies to Singapore Dollars\n"
            + "Parameters: AMOUNT [MORE_AMOUNTS]"
            + "Example: " + COMMAND_WORD + " USD 20.00  KRW 35.50  MYR 130.60";

    public static final String MESSAGE_SUCCESS = "Converted Amounts: %s";

    private final List<String> amounts;

    public ConvertCommand(List<String> amounts) {
        this.amounts = amounts;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        StringBuilder convertedAmounts = new StringBuilder();
        for (int i = 0; i < amounts.size(); i += 2) {
            Amount currentAmount = new Amount(amounts.get(i) + " " + amounts.get(i + 1));
            convertedAmounts.append(Amount.convertCurrency(currentAmount)).append(" ");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, convertedAmounts));
    }
}

