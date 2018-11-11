package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsLettersPredicate;

/**
 * Finds and lists any contacts whose names contain the substring the user has input.
 * Keyword matching is case insensitive.
 */
public class WildcardSearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Performs a wildcard search on the Debt Tracker's "
            + "contacts in either the current or past transaction list, based on user's input. "
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " oh\n"
            + "OR: " + COMMAND_WORD + " past " + "oh";

    private final String whichList;

    private final NameContainsLettersPredicate predicate;

    public WildcardSearchCommand(String whichList, NameContainsLettersPredicate predicate) {
        this.whichList = whichList;
        this.predicate = predicate;
    }

    public WildcardSearchCommand(NameContainsLettersPredicate predicate) {
        this.whichList = "";
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        if ("past".equals(whichList)) {
            model.updateFilteredPastTransactionList(predicate);
            model.commitFinancialDatabase();
            return new CommandResult(
                    String.format(Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW,
                            model.getFilteredPastTransactionList().size()));
        } else {
            model.updateFilteredTransactionList(predicate);
            model.commitFinancialDatabase();
            return new CommandResult(
                    String.format(Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW,
                            model.getFilteredTransactionList().size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WildcardSearchCommand)) {
            return false;
        }
        WildcardSearchCommand command = (WildcardSearchCommand) other;
        return command == this
                || predicate.equals(command.predicate);
    }
}
