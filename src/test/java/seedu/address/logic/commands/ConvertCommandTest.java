package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithModelChange;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithNoModelChange;
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
        String expectedMessage = ConvertCommand.MESSAGE_NO_TRANSACTION_AMOUNTS;
        Model expectedModel = new ModelManager();
        ConvertCommand convertCommand = new ConvertCommand();
        assertCommandFailureWithNoModelChange(convertCommand, model, expectedModel, history, expectedMessage);

        //executing convert command with a few transactions
        expectedMessage = ConvertCommand.MESSAGE_SUCCESS;
        Transaction firstTransaction = TypicalTransactions.AMY_TRANSACTION;
        Transaction secondTransaction = TypicalTransactions.BENSON_TRANSACTION;
        model.addTransaction(firstTransaction);
        model.addTransaction(secondTransaction);
        assertCommandSuccessWithModelChange(convertCommand, model, history, expectedMessage);

        //executing convert command with a transaction whose converted amount that will cause duplication
        Transaction thirdTransaction = Transaction.copy(firstTransaction);
        expectedMessage = ConvertCommand.MESSAGE_CONVERT_CAUSING_DUPLICATION;
        thirdTransaction.setAmount(new Amount("USD 105.57"));
        model.addTransaction(thirdTransaction);
        assertCommandFailureWithModelChange(convertCommand, model, history, expectedMessage);
    }
}
