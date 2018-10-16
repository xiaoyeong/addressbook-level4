package seedu.address.logic.commands;

import seedu.address.commons.core.CalendarManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;


/**
 * Lists all persons in the address book to the user.
 */
public class CalendarCommand extends Command {

    public static final String COMMAND_WORD = "calendar";
    public static final String COMMAND_ALIAS = "cal";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays a calendar showing the user's transactions "
            + "and their deadlines \n"
            + "Examples: \n"
            + COMMAND_WORD + " show\n"
            + COMMAND_WORD + " login\n"
            + COMMAND_WORD + " logout\n";

    public static final String MESSAGE_SUCCESS = "Calendar opened";

    private final String action;

    public CalendarCommand(String action){ this.action = action; }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        switch (action) {
            case "login":
                if(CalendarManager.getInstance().calendarLogin()) {
                    return new CommandResult("");
                } else {
                    return new CommandResult("Already logged in.");
                }
            case "logout":
                if(!CalendarManager.getInstance().isAuthenticated()) {
                    return new CommandResult("Not logged in.");
                } else {
                    if(CalendarManager.getInstance().calendarLogout()){
                        return new CommandResult("Logged out");
                    } else {
                        return new CommandResult("Failed to logout");
                    }
                }
            case "show" :
                String msg;
                if (CalendarManager.getInstance().isAuthenticated()) {
                    EventsCenter.getInstance()
                            .post(new ShowCalendarEvent(CalendarManager.getInstance().getCalendarId()));
                    msg = "Calendar loaded";
                } else {
                    msg = "Not logged in. Please enter the following command to login:\ncalendar login";
                }
                return new CommandResult(msg);
            default:
                return new CommandResult("");
        }
    }
}
