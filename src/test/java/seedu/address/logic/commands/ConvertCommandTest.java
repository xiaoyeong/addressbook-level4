package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.transaction.Amount;


public class ConvertCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    @Test
    public void execute_multipleAmounts_multipleCurrenciesConverted() {
        String[] amounts = {"USD", "45.20", "AUD", "54.60", "MYR", "33.40"};
        StringBuilder expectedMessage = new StringBuilder("Converted Amounts: ");
        for (int i = 0; i < amounts.length; i += 2) {
            expectedMessage.append(Amount.convertCurrency(new Amount(amounts[i] + " " + amounts[i + 1]))).append(" ");
        }
        ConvertCommand convertCommand = new ConvertCommand(Arrays.asList(amounts));
        assertCommandSuccess(convertCommand, model, history, expectedMessage.toString(), expectedModel);
    }
}
