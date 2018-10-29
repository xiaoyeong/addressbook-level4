package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


public class SortCommandTest {
    private static final int LIST_SIZE = getTypicalFinancialDatabase().getTransactionList().size();
    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_lowercaseTypeSortParameter_success() {
        String typeSortParameter = "type";
        SortCommand sortCommand = new SortCommand(typeSortParameter);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, LIST_SIZE, typeSortParameter);
        assertCommandSuccessWithModelChange(sortCommand, model, commandHistory, expectedMessage);
    }
    @Test
    public void execute_lowercaseDeadlineSortParameter_success() {
        String deadlineSortParameter = "deadline";
        SortCommand sortCommand = new SortCommand(deadlineSortParameter);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, LIST_SIZE, deadlineSortParameter);
        assertCommandSuccessWithModelChange(sortCommand, model, commandHistory, expectedMessage);
    }
    @Test
    public void execute_lowercaseAmountSortParameter_success() {
        String amountSortParameter = "amount";
        SortCommand sortCommand = new SortCommand(amountSortParameter);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, LIST_SIZE, amountSortParameter);
        assertCommandSuccessWithModelChange(sortCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_noSortParameter_success() {
        String amountSortParameter = "";
        SortCommand sortCommand = new SortCommand(amountSortParameter);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, LIST_SIZE,
                SortCommand.DEFAULT_SORT_PARAMETER);
        assertCommandSuccessWithModelChange(sortCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_singleInvalidSortParameter_success() {
        String amountSortParameter = "invalid";
        SortCommand sortCommand = new SortCommand(amountSortParameter);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, LIST_SIZE,
                SortCommand.DEFAULT_SORT_PARAMETER);
        assertCommandSuccessWithModelChange(sortCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_multipleInvalidSortParameters_success() {
        String amountSortParameter = "invalid sort parameters";
        SortCommand sortCommand = new SortCommand(amountSortParameter);
        String expectedMessage = String.format(SortCommand.MESSAGE_SUCCESS, LIST_SIZE,
                SortCommand.DEFAULT_SORT_PARAMETER);
        assertCommandSuccessWithModelChange(sortCommand, model, commandHistory, expectedMessage);
    }
}
