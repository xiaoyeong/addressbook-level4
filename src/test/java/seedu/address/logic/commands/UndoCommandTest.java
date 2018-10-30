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

public class UndoCommandTest {

    private final Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of models' undo/redo history
        deleteFirstTransaction(model);
        deleteFirstTransaction(model);

        deleteFirstTransaction(expectedModel);
        deleteFirstTransaction(expectedModel);
    }

    @Test
    public void execute() {
        // multiple undoable states in model
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single undoable state in model
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no undoable states in model
        assertCommandFailureWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_FAILURE);
    }
}
