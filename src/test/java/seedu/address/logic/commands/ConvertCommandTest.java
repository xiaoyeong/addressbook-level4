package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;


public class ConvertCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    @Test
    public void execute_multipleAmounts_multipleCurrenciesConverted() {
        String[] amounts = {"USD", "45.20", "AUD", "54.60", "MYR", "33.40"};
        String expectedMessage = "Converted Amounts: SGD 62.35 SGD 53.69 SGD 11.06";
        ConvertCommand convertCommand = new ConvertCommand(Arrays.asList(amounts));
        assertCommandSuccess(convertCommand, model, history, expectedMessage, expectedModel);
    }
}
