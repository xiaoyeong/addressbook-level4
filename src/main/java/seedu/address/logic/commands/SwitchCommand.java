package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwitchTabsEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;


/**
 * Allows the user to switch between the Current Transactions tab and the Past Transactions tab.
 */
public class SwitchCommand extends Command {
    public static final String COMMAND_WORD = "switch";
    public static final String COMMAND_ALIAS = "sw";

    public static final String MESSAGE_PAID_SUCCESS = "Listed all paid transactions.";
    public static final String MESSAGE_CURRENT_SUCCESS = "Listed all pending transactions.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches between Current Transactions tab and "
            + "Past Transactions tab.\n"
            + "Examples: \n"
            + COMMAND_WORD + " curr\n"
            + COMMAND_WORD + " past";

    private final String whichTab;

    public SwitchCommand(String whichTab) {
        this.whichTab = whichTab;
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPastTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        switch(whichTab) {
        case "past":
            EventsCenter.getInstance().post(new SwitchTabsEvent(1));
            return new CommandResult(MESSAGE_PAID_SUCCESS);
        case "curr":
            EventsCenter.getInstance().post(new SwitchTabsEvent(0));
            return new CommandResult(MESSAGE_CURRENT_SUCCESS);
        default:
            return new CommandResult("");
        }
    }


}
