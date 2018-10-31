package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.transaction.NameContainsLettersPredicate;

/**
 * Finds and lists any contacts whose names contain the substring the user has input.
 * Keyword matching is case insensitive.
 */
public class WildcardSearchCommand extends Command {

    public static final String COMMAND_WORD = "search";
    public static final String COMMAND_ALIAS = "wcs";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Performs a wildcard search on the address book's "
            + "contacts based on user's input. "
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " oh";

    private final NameContainsLettersPredicate predicate;

    public WildcardSearchCommand(NameContainsLettersPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredTransactionList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW,
                        model.getFilteredTransactionList().size()));
    }
}
