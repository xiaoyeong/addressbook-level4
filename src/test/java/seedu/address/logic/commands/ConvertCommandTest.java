package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithModelChange;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.transaction.Amount;
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
        model.addTransaction(firstTransaction);
        assertCommandSuccessWithModelChange(convertCommand, model, history, expectedMessage);

        Transaction secondTransaction = TypicalTransactions.BENSON_TRANSACTION;
        secondTransaction.setAmount(new Amount("GBP 945.67"));
        assertCommandFailureWithModelChange(convertCommand, model, history, expectedMessage);

    }
}
