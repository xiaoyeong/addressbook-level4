package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Transaction;


public class PaidCommandTest {
    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Transaction personWhoPaid = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_TRANSACTION);

        String expectedMessage = String.format(PaidCommand.MESSAGE_PAID_TRANSACTION_SUCCESS, personWhoPaid);

        ModelManager expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.addPastTransaction(personWhoPaid);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithModelChange(paidCommand, model, commandHistory, expectedMessage);
    }
}
