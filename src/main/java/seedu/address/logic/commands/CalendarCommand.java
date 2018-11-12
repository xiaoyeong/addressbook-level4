package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.CalendarManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

/**
 * Enables the user to view the Google Calendar, log in to the service, and manually synchronize the calendar data.
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
    public static final String MESSAGE_SYNC_FAILURE = "Failed to sync calendar. Please check your internet connection.";
    public static final String MESSAGE_ACCESS_FAILURE = "Error accessing calendar. "
                        + "Please check your internet connection.";

    private final String action;

    public CalendarCommand(String action) {
        this.action = action;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        CalendarManager calendarManager = CalendarManager.getInstance();
        switch (action) {
        case LOGIN_ACTION:
            if (calendarManager.calendarLogin(model)) {
                return new CommandResult("");
            } else {
                return new CommandResult("Already logged in.");
            }
        case LOGOUT_ACTION:
            if (!calendarManager.isAuthenticated()) {
                return new CommandResult("Not logged in.");
            } else {
                if (CalendarManager.getInstance().calendarLogout()) {
                    return new CommandResult("Logged out");
                } else {
                    return new CommandResult("Failed to logout");
                }
            }
        case SHOW_ACTION:
            if (calendarManager.isAuthenticated()) {
                if (!calendarManager.initializeCalendar()) {
                    return new CommandResult(MESSAGE_ACCESS_FAILURE);
                }
                EventsCenter.getInstance()
                        .post(new ShowCalendarEvent(CalendarManager.getInstance().getCalendarId()));
                return new CommandResult("");
            } else {
                calendarManager.calendarLogin(model);
                return new CommandResult("Not logged in. "
                        + "Please login now or enter the following command to login:\ncalendar login");
            }
        case SYNC_ACTION:
            if (calendarManager.isAuthenticated()) {
                if (!calendarManager.initializeCalendar()) {
                    return new CommandResult(MESSAGE_ACCESS_FAILURE);
                }
                CalendarManager.SyncResult result = calendarManager.syncCalendar(model);
                if (result == null) {
                    return new CommandResult(String.format(MESSAGE_SYNC_FAILURE));
                } else {
                    return new CommandResult(String.format(MESSAGE_SYNC_SUCCESS, result));
                }
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
        return other == this // short circuit if same object
                || (other instanceof CalendarCommand // instanceof handles nulls
                && action.equals(((CalendarCommand) other).action));
    }
}
