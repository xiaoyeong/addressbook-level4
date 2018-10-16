package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.MainApp;
import seedu.address.Mode;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Switches between transaction and transaction modes
 */
public class ModeCommand extends Command {

    public static final String COMMAND_WORD = "mode";
    public static final String COMMAND_ALIAS = "m";
    public static final String MESSAGE_PERSON_MODE = "Switched to Person Mode.";
    public static final String MESSAGE_TRANSACTION_MODE = "Switched to Transaction Mode.";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        boolean isCorrectMode = MainApp.checkMode(Mode.TransactionMode);
        if (isCorrectMode) {
            MainApp.setMode(Mode.PersonMode);
            return new CommandResult(MESSAGE_PERSON_MODE);
        } else {
            MainApp.setMode(Mode.TransactionMode);
            return new CommandResult(MESSAGE_TRANSACTION_MODE);
        }
    }
}
