package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.transaction.FieldContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name/phone/email/address contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";
    public static final String COMMAND_ALIAS = "fi";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters all transactions whose specified field(s) "
            + "contain any of the specified keywords (case-insensitive) and displays them as a list"
            + ".\nParameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " n/alice e/alice@gmail.com";

    private final List<FieldContainsKeywordsPredicate> predicates;

    public FilterCommand(List<FieldContainsKeywordsPredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        for (int i = 0; i < predicates.size(); i++) {
            model.updateFilteredTransactionList(predicates.get(i));
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredTransactionList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FilterCommand // instanceof handles nulls
                && predicates.equals(((FilterCommand) other).predicates)); // state check
    }
}
