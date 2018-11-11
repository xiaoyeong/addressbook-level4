package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_ALL_TRANSACTIONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
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
        String expectedMessage = String.format(MESSAGE_ALL_TRANSACTIONS_LISTED_OVERVIEW, 0, 0);
        FieldContainsKeywordsPredicate predicate = preparePredicate("no one");
        FilterCommand command = new FilterCommand(Collections.singletonList(predicate),
                MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(predicate);
        expectedModel.updateFilteredPastTransactionList(predicate);
        assertCommandSuccessWithModelChange(command, model, commandHistory, expectedMessage);
        assertEquals(Collections.emptyList(), model.getFilteredTransactionList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_ALL_TRANSACTIONS_LISTED_OVERVIEW, 3, 0);
        FieldContainsKeywordsPredicate predicate = preparePredicate("Kurz;Elle;Kunz");
        FilterCommand command = new FilterCommand(Collections.singletonList(predicate),
                MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(predicate);
        expectedModel.updateFilteredPastTransactionList(predicate);
        assertCommandSuccessWithModelChange(command, model, commandHistory, expectedMessage);
        assertEquals(Arrays.asList(CARL_TRANSACTION, ELLE_TRANSACTION, FIONA_TRANSACTION),
                model.getFilteredTransactionList());
    }

    @Test
    public void execute_multiplePrefixes_singleTransactionFound() {
        String expectedMessage = String.format(MESSAGE_ALL_TRANSACTIONS_LISTED_OVERVIEW, 1, 0);
        FieldContainsKeywordsPredicate namePredicate = preparePredicate("pauline");
        FieldContainsKeywordsPredicate addressPredicate =
                new FieldContainsKeywordsPredicate(FieldType.Address, Collections.singletonList("jurong"));
        FieldContainsKeywordsPredicate emailPredicate =
                new FieldContainsKeywordsPredicate(FieldType.Email, Collections.singletonList("example.com"));
        FieldContainsKeywordsPredicate phonePredicate =
                new FieldContainsKeywordsPredicate(FieldType.Phone, Collections.singletonList("94351253"));
        FieldContainsKeywordsPredicate tagPredicate =
                new FieldContainsKeywordsPredicate(FieldType.Tag, Collections.singletonList("friends"));
        FieldContainsKeywordsPredicate amountPredicate =
                new FieldContainsKeywordsPredicate(FieldType.Amount, Collections.singletonList("42.50"));
        FieldContainsKeywordsPredicate typePredicate =
                new FieldContainsKeywordsPredicate(FieldType.Type, Collections.singletonList("loan"));
        List<Predicate<Transaction>> predicates = Arrays.asList(namePredicate, addressPredicate, emailPredicate,
                phonePredicate, tagPredicate, amountPredicate, typePredicate);

        MultiFieldPredicate multiPredicate = new MultiFieldPredicate(predicates, MultiFieldPredicate.OperatorType.AND);
        FilterCommand command = new FilterCommand(predicates,
                MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(multiPredicate);
        expectedModel.updateFilteredPastTransactionList(multiPredicate);
        assertCommandSuccessWithModelChange(command, model, commandHistory, expectedMessage);
        assertEquals(Collections.singletonList(ALICE_TRANSACTION),
                model.getFilteredTransactionList());
    }

    @Test
    public void execute_betweenTwoDates_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_ALL_TRANSACTIONS_LISTED_OVERVIEW, 7, 0);
        DeadlineBoundsPredicate earliestDatePredicate =
                new DeadlineBoundsPredicate(new Deadline("10/10/2018"), DeadlineBoundsPredicate.BoundType.EARLIEST);
        DeadlineBoundsPredicate latestDatePredicate =
                new DeadlineBoundsPredicate(new Deadline("10/10/2050"), DeadlineBoundsPredicate.BoundType.LATEST);
        List<Predicate<Transaction>> predicates = Arrays.asList(earliestDatePredicate, latestDatePredicate);
        FilterCommand command = new FilterCommand(predicates, MultiFieldPredicate.OperatorType.AND);
        MultiFieldPredicate multiPredicate = new MultiFieldPredicate(predicates, MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(multiPredicate);
        expectedModel.updateFilteredPastTransactionList(multiPredicate);
        assertCommandSuccessWithModelChange(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_betweenTwoAmounts_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_ALL_TRANSACTIONS_LISTED_OVERVIEW, 5, 0);
        AmountBoundsPredicate minAmount = new AmountBoundsPredicate(new Amount("SGD 10.00"),
                AmountBoundsPredicate.BoundType.MIN);
        AmountBoundsPredicate maxAmount = new AmountBoundsPredicate(new Amount("SGD 50.00"),
                AmountBoundsPredicate.BoundType.MAX);
        List<Predicate<Transaction>> predicates = Arrays.asList(minAmount, maxAmount);
        FilterCommand command = new FilterCommand(predicates, MultiFieldPredicate.OperatorType.AND);
        MultiFieldPredicate multiPredicate = new MultiFieldPredicate(predicates, MultiFieldPredicate.OperatorType.AND);
        expectedModel.updateFilteredTransactionList(multiPredicate);
        expectedModel.updateFilteredPastTransactionList(multiPredicate);
        assertCommandSuccessWithModelChange(command, model, commandHistory, expectedMessage);
    }

    /**
     * Parses {@code userInput} into a {@code FieldContainsKeywordsPredicate}.
     */
    private FieldContainsKeywordsPredicate preparePredicate(String userInput) {
        return new FieldContainsKeywordsPredicate(FieldType.Name, Arrays.asList(userInput.split(";")));
    }

}
