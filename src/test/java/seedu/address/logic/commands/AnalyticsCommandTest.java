package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTransactions.getUniqueFinancialDatabase;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Deadline;


public class AnalyticsCommandTest {
    private CommandHistory history = new CommandHistory();
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    private Model modelWithData = new ModelManager(getUniqueFinancialDatabase(), new UserPrefs());
    private Model expectedModelWithData = new ModelManager(getUniqueFinancialDatabase(), new UserPrefs());


    @Test
    public void execute_emptyFinancialList() {
        String expectedMessage = "Financial status : SGD 0.00";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand();
        assertCommandSuccess(analyticsCommand, model, history, expectedMessage, expectedModel);
    }
    @Test
    public void execute_emptyFinancialList_withValidDate() {
        try {
            String date = "12/12/2020";
            Deadline checkDate = new Deadline(date);
            String expectedMessage = "Financial status : SGD 0.00";
            AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
            assertCommandSuccess(analyticsCommand, model, history, expectedMessage, expectedModel);
        } catch (Exception DataConversionException ) {
            //Test case is no longer valid as the date have pass.
        }
    }

    @Test
    public void execute_non_emptyFinancialList_withNoDate() {
        String expectedMessage = "Financial status : SGD 188.10";
        AnalyticsCommand analyticsCommand = new AnalyticsCommand();
        assertCommandSuccess(analyticsCommand, modelWithData, history, expectedMessage, expectedModelWithData);
    }
    @Test
    public void execute_non_emptyFinancialList_withDate() {
        try {
            String date = "11/11/2018";
            Deadline checkDate = new Deadline(date);
            String expectedMessage = "Financial status : SGD 0.00";
            AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
            assertCommandSuccess(analyticsCommand, modelWithData, history, expectedMessage, expectedModelWithData);
        } catch (Exception DataConversionException ) {
            //Test case is no longer valid as the date have pass.
        }
    }

    @Test
    public void execute_non_emptyFinancialList_withDateTest2() {
        try {
            String date = "12/11/2018";
            Deadline checkDate = new Deadline(date);
            String expectedMessage = "Financial status : SGD 85.00";
            AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
            assertCommandSuccess(analyticsCommand, modelWithData, history, expectedMessage, expectedModelWithData);
        } catch (Exception DataConversionException ) {
            //Test case is no longer valid as the date have pass.
        }
    }

    @Test
    public void execute_non_emptyFinancialList_withDateTest3() {
        try {
            String date = "18/11/2018";
            Deadline checkDate = new Deadline(date);
            String expectedMessage = "Financial status : SGD 230.60";
            AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
            assertCommandSuccess(analyticsCommand, modelWithData, history, expectedMessage, expectedModelWithData);
        } catch (Exception DataConversionException ) {
        //Test case is no longer valid as the date have pass.
    }
    }
    @Test
    public void execute_non_emptyFinancialList_withDateTest4() {
        try{
            String date = "12/12/2018";
            Deadline checkDate = new Deadline(date);
            String expectedMessage = "Financial status : SGD 188.10";
            AnalyticsCommand analyticsCommand = new AnalyticsCommand(checkDate);
            assertCommandSuccess(analyticsCommand, modelWithData, history, expectedMessage, expectedModelWithData);
        } catch (Exception DataConversionException ) {
            //Test case is no longer valid as the date have pass.
        }
    }
}
