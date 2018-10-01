package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class AnalyticsCommand extends Command {
    public static final String COMMAND_WORD = "Analytics";
    public static final String COMMAND_ALIAS = "an";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Analyse the your financial status and generate \n"
            + "your expected income for the following week.\n";

    public static final String MESSAGE_SUCCESS = "Financial status : $ ";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        int amount;
        amount = 0;
        requireNonNull(model);
        List<Person> personList = model.getFilteredPersonList();

        for (int i = 0; i < personList.size(); i++) {
            amount += personList.ownesMoney();
        }


        return new CommandResult(String.format(MESSAGE_SUCCESS, amount));
    }
}

