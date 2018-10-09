package seedu.address.logic.commands;

import seedu.address.MainApp;
import seedu.address.Mode;
import seedu.address.logic.CommandHistory;
import seedu.address.model.FinancialDatabase;
import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;


/**
 * Clears the address book.
 */
public class ModeCommand extends Command {

    public static final String COMMAND_WORD = "mode";
    public static final String COMMAND_ALIAS = "m";
    public static final String MESSAGE_PERSON_MODE = "Switched to Person Mode.";
    public static final String MESSAGE_TRANSACTION_MODE = "Switched to Transaction Mode.";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        if (MainApp.m == Mode.TransactionMode) {
            MainApp.m = Mode.PersonMode;
            return new CommandResult(MESSAGE_PERSON_MODE);
        } else {
            MainApp.m = Mode.TransactionMode;
            return new CommandResult(MESSAGE_TRANSACTION_MODE);
        }
    }
}
