package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithNoModelChange;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class InterestCommandTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_noTransactions_throwsIllegalArgumentException() {
        Model expectedModel = new ModelManager();
        String expectedMessage = InterestCommand.MESSAGE_NO_TRANSACTIONS;
        InterestCommand interestCommand = new InterestCommand("simple", "1.50%");
        ModelManager actualModel = new ModelManager();
        assertCommandFailureWithNoModelChange(interestCommand, actualModel, expectedModel, commandHistory,
                expectedMessage);
    }

    @Test
    public void execute_typicalTransactions_success() {
        Model expectedModel = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
        int sizeOfTransactionList = model.getFilteredTransactionList().size();
        String expectedMessage = String.format(InterestCommand.MESSAGE_SUCCESS, sizeOfTransactionList);
        InterestCommand interestCommand = new InterestCommand("compound", "2.30%");
        assertCommandSuccessWithModelChange(interestCommand, model, commandHistory, expectedMessage);
    }
}
