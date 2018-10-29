package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithNoModelChange;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithNoModelChange;
import static seedu.address.logic.commands.CommandTestUtil.showTransactionAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Transaction;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Transaction personToDelete = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRANSACTION);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRANSACTION_SUCCESS, personToDelete);

        ModelManager expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.deleteTransaction(personToDelete);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithNoModelChange(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailureWithNoModelChange(deleteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Transaction personToDelete = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRANSACTION);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRANSACTION_SUCCESS, personToDelete);

        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.deleteTransaction(personToDelete);
        expectedModel.commitFinancialDatabase();
        showNoPerson(expectedModel);

        assertCommandSuccessWithNoModelChange(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Index outOfBoundIndex = INDEX_SECOND_TRANSACTION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFinancialDatabase().getTransactionList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailureWithNoModelChange(deleteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Transaction personToDelete = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRANSACTION);
        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.deleteTransaction(personToDelete);
        expectedModel.commitFinancialDatabase();

        // delete -> first transaction deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first transaction deleted again
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        // execution failed -> address book state not added into model
        assertCommandFailureWithNoModelChange(deleteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailureWithNoModelChange(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailureWithNoModelChange(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Person} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted transaction in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the transaction object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_TRANSACTION);
        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());

        showTransactionAtIndex(model, INDEX_SECOND_TRANSACTION);
        Transaction personToDelete = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        expectedModel.deleteTransaction(personToDelete);
        expectedModel.commitFinancialDatabase();

        // delete -> deletes second transaction in unfiltered transaction list/ first
        // transaction in filtered transaction list
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(personToDelete, model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased()));
        // redo -> deletes same second transaction in unfiltered transaction list
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_TRANSACTION);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_TRANSACTION);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_TRANSACTION);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different transaction -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredTransactionList(p -> false);

        assertTrue(model.getFilteredTransactionList().isEmpty());
    }
}
