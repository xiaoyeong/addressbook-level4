package seedu.address.logic.commands;

import java.io.IOException;
import java.util.List;

import seedu.address.commons.core.CalendarManager;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

/**
 * Sets a reminder for an existing transaction in the database.
 */
public class ReminderCommand extends Command {
    public static final String COMMAND_WORD = "remind";
    public static final String COMMAND_ALIAS = "rem";
    public static final String DEFAULT_REMINDER_PERIOD = "30 minutes";
    public static final int DEFAULT_INDEX = 1;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a reminder for a transaction specified by "
            + "an index based on the given time period.\n"
            + "Parameters: INDEX TIME_PERIOD\n"
            + "Examples: \n"
            + COMMAND_WORD + " " + DEFAULT_INDEX + " " + DEFAULT_REMINDER_PERIOD;
    public static final String MESSAGE_SUCCESS = "Set reminder for %s transaction.";

    private final int index;
    private final int timePeriod;

    /**
     * @param index of the transaction in the filtered transaction list to edit
     * @param timePeriod period of time before a transaction deadline for which to set the reminder
     */
    public ReminderCommand(int index, int timePeriod) {
        this.index = index;
        this.timePeriod = timePeriod;
    }
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Transaction> lastShownList = model.getFilteredTransactionList();
        Transaction transactionToSetReminder = lastShownList.get(index);
        if (!CalendarManager.getInstance().isAuthenticated()) {
            return new CommandResult("You have to first login using calendar command.");
        }
        try {
            CalendarManager.getInstance().setReminder(timePeriod, transactionToSetReminder);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String transactionIndexFormat;
        switch (index) {
        case 1:
            transactionIndexFormat = index + "st";
            break;
        case 2:
            transactionIndexFormat = index + "nd";
            break;
        case 3:
            transactionIndexFormat = index + "rd";
            break;
        default:
            transactionIndexFormat = index + "th";
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, transactionIndexFormat));
    }
}
