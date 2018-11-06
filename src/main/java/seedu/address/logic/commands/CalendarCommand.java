package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.CalendarManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class CalendarCommand extends Command {

    public static final String COMMAND_WORD = "calendar";
    public static final String SHOW_ACTION = "show";
    public static final String LOGIN_ACTION = "login";
    public static final String LOGOUT_ACTION = "logout";
    public static final String SYNC_ACTION = "sync";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays a calendar showing the user's transactions "
            + "and their deadlines \n"
            + "Examples: \n"
            + COMMAND_WORD + " " + SHOW_ACTION + "\n"
            + COMMAND_WORD + " " + LOGIN_ACTION + " \n"
            + COMMAND_WORD + " " + LOGOUT_ACTION + "\n"
            + COMMAND_WORD + " " + SYNC_ACTION + "\n";

    public static final String MESSAGE_SYNC_SUCCESS = "Calendar Synced. %1$s";

    private final String action;

    public CalendarCommand(String action) {
        this.action = action;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        switch (action) {
        case LOGIN_ACTION:
            if (CalendarManager.getInstance().calendarLogin(model)) {
                return new CommandResult("");
            } else {
                return new CommandResult("Already logged in.");
            }
        case LOGOUT_ACTION:
            if (!CalendarManager.getInstance().isAuthenticated()) {
                return new CommandResult("Not logged in.");
            } else {
                if (CalendarManager.getInstance().calendarLogout()) {
                    return new CommandResult("Logged out");
                } else {
                    return new CommandResult("Failed to logout");
                }
            }
        case SHOW_ACTION:
            if (CalendarManager.getInstance().isAuthenticated()) {
                EventsCenter.getInstance()
                        .post(new ShowCalendarEvent(CalendarManager.getInstance().getCalendarId()));
                return new CommandResult("Calendar loaded");
            } else {
                CalendarManager.getInstance().calendarLogin(model);
                return new CommandResult("Not logged in. "
                        + "Please login now or enter the following command to login:\ncalendar login");
            }
        case SYNC_ACTION:
            if (CalendarManager.getInstance().isAuthenticated()) {
                CalendarManager.SyncResult result = CalendarManager.getInstance().syncCalendar(model);
                return new CommandResult(String.format(MESSAGE_SYNC_SUCCESS, result));
            } else {
                return new CommandResult("Not logged in. "
                        + "Please enter the following command to login:\ncalendar login");
            }
        default:
            return new CommandResult("");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CalendarCommand)) {
            return false;
        }

        // state check
        CalendarCommand calendarCommand = (CalendarCommand) other;
        return action.equals(calendarCommand.action);
    }
}
