package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithNoModelChange;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.TransactionBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Transaction validTransaction = new TransactionBuilder().build();

        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.addTransaction(validTransaction);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithNoModelChange(new AddCommand(validTransaction), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validTransaction), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Transaction transactionInList = model.getFinancialDatabase().getTransactionList().get(0);
        assertCommandFailureWithNoModelChange(new AddCommand(transactionInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

}
