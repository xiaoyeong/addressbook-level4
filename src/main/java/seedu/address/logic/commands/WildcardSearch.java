package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsLettersPredicate;

/**
 *
 */
public class WildcardSearch extends Command{

    public static final String COMMAND_WORD = "search";
    public static final String COMMAND_ALIAS = "wcs";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Performs a wildcard search on the address book's "
            + "contacts based on user's input."
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " oh";

    private final NameContainsLettersPredicate predicate;

    public WildcardSearch(NameContainsLettersPredicate predicate) { this.predicate = predicate; }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

}
