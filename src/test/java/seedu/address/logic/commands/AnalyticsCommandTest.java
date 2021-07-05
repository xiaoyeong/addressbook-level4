package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithModelChange;
import static seedu.address.testutil.TypicalTransactions.getUniqueFinancialDatabase;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Deadline;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AnalyticsCommand.
 */
public class AnalyticsCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    private Model modelWithData = new ModelManager(getUniqueFinancialDatabase(), new UserPrefs());


    @Test
    public void execute_emptyFinancialListWithNoDate() {
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand();
        assertCommandSuccessWithModelChange(analyticsCommand, model, history, expectedMessage);
    }

    @Test
    public void execute_emptyFinancialListWithDate_whenDatePassedThenTestWillFail() {
        String date = "12/12/2020";
        Deadline checkDate = new Deadline(date);
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
        assertCommandSuccessWithModelChange(analyticsCommand, model, history, expectedMessage);
    }

    @Test
    public void execute_non_emptyFinancialListWithNoDate() {
        String expectedMessage = "Financial status : SGD 188.10";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand();
        assertCommandSuccessWithModelChange(analyticsCommand, modelWithData, history, expectedMessage);
    }

    @Test
    public void execute_nonEmptyFinancialListWithDate_whenDatePassedThenTestWillFail() {
        String date = "11/11/2018";
        Deadline checkDate = new Deadline(date);
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
        assertCommandSuccessWithModelChange(analyticsCommand, modelWithData, history, expectedMessage);
    }

    @Test
    public void execute_nonEmptyFinancialListWithDateTest2_whenDatePassedThenTestWillFail() {
        String date = "12/11/2018";
        Deadline checkDate = new Deadline(date);
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
        assertCommandSuccessWithModelChange(analyticsCommand, modelWithData, history, expectedMessage);
    }

    @Test
    public void execute_nonEmptyFinancialListWithDateTest3_whenDatePassedThenTestWillFail() {
        String date = "18/11/2018";
        Deadline checkDate = new Deadline(date);
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
        assertCommandSuccessWithModelChange(analyticsCommand, modelWithData, history, expectedMessage);
    }

    @Test
    public void execute_nonEmptyFinancialListWithDateTest4_whenDatePassedThenTestWillFail() {
        String date = "12/12/2018";
        Deadline checkDate = new Deadline(date);
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
        assertCommandSuccessWithModelChange(analyticsCommand, modelWithData, history, expectedMessage);
    }
}
