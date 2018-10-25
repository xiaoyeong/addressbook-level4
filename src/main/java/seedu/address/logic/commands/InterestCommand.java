package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Calculates an interest rate for a given transaction (either using simple or compound scheme as specified by the
 * user).
 */
public class InterestCommand extends Command {
    public static final String COMMAND_WORD = "interest";
    public static final String COMMAND_ALIAS = "int";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        return null;
    }
}
