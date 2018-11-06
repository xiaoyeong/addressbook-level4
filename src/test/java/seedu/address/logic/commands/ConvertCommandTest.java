package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.TypicalTransactions;


public class ConvertCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    @Test
    public void execute() {
        //executing convert command with no transactions
        String expectedMessage = ConvertCommand.MESSAGE_SUCCESS;
        ConvertCommand convertCommand = new ConvertCommand();
        assertCommandSuccessWithModelChange(convertCommand, model, history, expectedMessage);

        //executing convert command with a few transactions
        Transaction firstTransaction = TypicalTransactions.AMY_TRANSACTION;
        Transaction secondTransaction = TypicalTransactions.BENSON_TRANSACTION;
        model.addTransaction(firstTransaction);
        model.addTransaction(secondTransaction);
        assertCommandSuccessWithModelChange(convertCommand, model, history, expectedMessage);
    }
}
