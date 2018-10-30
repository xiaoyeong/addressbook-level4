package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;
import static seedu.address.testutil.TypicalTransactions.CARL_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.ELLE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.FIONA_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.FieldType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.AmountBoundsPredicate;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.DeadlineBoundsPredicate;
import seedu.address.model.transaction.FieldContainsKeywordsPredicate;
import seedu.address.model.transaction.MultiFieldPredicate;
import seedu.address.model.transaction.Transaction;


/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        List<Predicate<Transaction>> firstPredicates = new ArrayList<>();
        firstPredicates.add(new FieldContainsKeywordsPredicate(FieldType.Name, Collections.singletonList("first")));
        firstPredicates.add(new AmountBoundsPredicate(new Amount("SGD 10.00"), AmountBoundsPredicate.BoundType.MAX));
        firstPredicates.add(new DeadlineBoundsPredicate(new Deadline("12/12/2018"),
                DeadlineBoundsPredicate.BoundType.LATEST));
        FilterCommand filterFirstCommand = new FilterCommand(firstPredicates, MultiFieldPredicate.OperatorType.OR);

        List<Predicate<Transaction>> secondPredicates = new ArrayList<>();
        firstPredicates.add(new FieldContainsKeywordsPredicate(FieldType.Name, Collections.singletonList("second")));
        firstPredicates.add(new AmountBoundsPredicate(new Amount("SGD 10.00"), AmountBoundsPredicate.BoundType.MAX));
        firstPredicates.add(new DeadlineBoundsPredicate(new Deadline("12/12/2018"),
                DeadlineBoundsPredicate.BoundType.LATEST));
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicates, MultiFieldPredicate.OperatorType.OR);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicates, MultiFieldPredicate.OperatorType.OR);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different transaction -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_TRANSACTIONS_LISTED_OVERVIEW, 0);
        FieldContainsKeywordsPredicate predicate = preparePredicate("no one");
        FilterCommand command = new FilterCommand(Collections.singletonList(predicate),
                MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(predicate);
        assertCommandSuccessWithNoModelChange(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredTransactionList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_TRANSACTIONS_LISTED_OVERVIEW, 3);
        FieldContainsKeywordsPredicate predicate = preparePredicate("Kurz;Elle;Kunz");
        FilterCommand command = new FilterCommand(Collections.singletonList(predicate),
                MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(predicate);
        assertCommandSuccessWithNoModelChange(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL_TRANSACTION, ELLE_TRANSACTION, FIONA_TRANSACTION),
                model.getFilteredTransactionList());
    }

    /**
     * Parses {@code userInput} into a {@code FieldContainsKeywordsPredicate}.
     */
    private FieldContainsKeywordsPredicate preparePredicate(String userInput) {
        return new FieldContainsKeywordsPredicate(FieldType.Name, Arrays.asList(userInput.split(";")));
    }
}
