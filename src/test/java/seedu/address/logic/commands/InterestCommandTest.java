package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.InterestRate;
import seedu.address.model.transaction.InterestScheme;

public class InterestCommandTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_noScheme_throwsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(InterestScheme.MESSAGE_INTEREST_SCHEME_CONSTRAINTS);
        InterestCommand interestCommand = new InterestCommand("", "1.5%");
        interestCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_wrongScheme_throwsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(InterestScheme.MESSAGE_INTEREST_SCHEME_CONSTRAINTS);
        InterestCommand interestCommand = new InterestCommand("compund", "1.5%");
        interestCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_noRate_throwsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(InterestRate.MESSAGE_INTEREST_RATE_CONSTRAINTS);
        InterestCommand interestCommand = new InterestCommand("simple", "");
        interestCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_wrongRate_throwsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(InterestRate.MESSAGE_INTEREST_RATE_CONSTRAINTS);
        InterestCommand interestCommand = new InterestCommand("compound", "1.5");
        interestCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_noSchemeAndRate_throwsIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(InterestScheme.MESSAGE_INTEREST_SCHEME_CONSTRAINTS);
        InterestCommand interestCommand = new InterestCommand("", "");
        interestCommand.execute(model, commandHistory);
    }

    @Test
    public void execute_schemeWrongCase_success() {
        int sizeOfTransactionList = model.getFilteredTransactionList().size();
        String expectedMessage = String.format(InterestCommand.MESSAGE_SUCCESS, sizeOfTransactionList);
        InterestCommand interestCommand = new InterestCommand("cOmPoUnD", "2.3%");
        assertCommandSuccessWithNoModelChange(interestCommand, model, commandHistory, expectedMessage, expectedModel);
    }
}
