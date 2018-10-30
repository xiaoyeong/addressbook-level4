package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;


public class ConvertCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    @Test
    public void execute_multipleCurrenciesConverted() {
        String expectedMessage = ConvertCommand.MESSAGE_SUCCESS;
        ConvertCommand convertCommand = new ConvertCommand();
        assertCommandSuccessWithNoModelChange(convertCommand, model, history, expectedMessage, expectedModel);
    }
}
