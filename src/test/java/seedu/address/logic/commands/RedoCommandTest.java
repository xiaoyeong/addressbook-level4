package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithNoModelChange;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;
import static seedu.address.logic.commands.CommandTestUtil.deleteFirstTransaction;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class RedoCommandTest {

    private final Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of both models' undo/redo history
        deleteFirstTransaction(model);
        deleteFirstTransaction(model);
        model.undoFinancialDatabase();
        model.undoFinancialDatabase();

        deleteFirstTransaction(expectedModel);
        deleteFirstTransaction(expectedModel);
        expectedModel.undoFinancialDatabase();
        expectedModel.undoFinancialDatabase();
    }

    @Test
    public void execute() {
        // multiple redoable states in model
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // single redoable state in model
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // no redoable state in model
        assertCommandFailureWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_FAILURE);
    }
}
