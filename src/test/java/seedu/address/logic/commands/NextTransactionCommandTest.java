package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_TRANSACTIONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.BOB_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.getUniqueFinancialDatabase;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.DeadlineContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) and unit tests for NextTransactionCommand.
 */
public class NextTransactionCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    private Model modelWithData = new ModelManager(getUniqueFinancialDatabase(), new UserPrefs());
    private Model expectedModelWithData = new ModelManager(getUniqueFinancialDatabase(), new UserPrefs());

    @Test
    public void execute_emptyFinancialList() {
        String expectedMessage = String.format(MESSAGE_TRANSACTIONS_LISTED_OVERVIEW, 0);
        NextTransactionCommand nextTransactionCommand = new NextTransactionCommand();
        assertCommandSuccessWithNoModelChange(nextTransactionCommand, model, history, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonEmptyFinancialList() {
        String expectedMessage = String.format(MESSAGE_TRANSACTIONS_LISTED_OVERVIEW, 2);
        DeadlineContainsKeywordsPredicate predicate = preparePredicate("12/11/2018");
        NextTransactionCommand command = new NextTransactionCommand();
        expectedModelWithData.updateFilteredTransactionList(predicate);
        assertCommandSuccessWithNoModelChange(command, modelWithData, history, expectedMessage, expectedModelWithData);
        assertEquals(Arrays.asList(ALICE_TRANSACTION, BOB_TRANSACTION),
                expectedModelWithData.getFilteredTransactionList());
    }

    private DeadlineContainsKeywordsPredicate preparePredicate(String userInput) {
        return new DeadlineContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
