package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Adds a String user input to the list. Contains actions that the user may want to take related to their
 * transactions.
 */
public class ToDoListAddCommand extends Command {
    public static final String COMMAND_WORD = "todo";
    public static final String COMMAND_ALIAS = "toDo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an action to the user's to-do list. "
            + "Example: "
            + COMMAND_WORD + " Buy some eggs.";

    private final String action;

    public ToDoListAddCommand(String action) {
        this.action = action;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        return new CommandResult(String.format(MESSAGE_USAGE));
    }
}
