package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailureWithModelChange;
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


public class PaidCommandTest {
    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Transaction personWhoPaid = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_TRANSACTION);

        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.deleteTransaction(personWhoPaid);
        expectedModel.addPastTransaction(personWhoPaid);
        expectedModel.commitFinancialDatabase();

        String expectedMessage = String.format(PaidCommand.MESSAGE_PAID_TRANSACTION_SUCCESS, personWhoPaid);

        assertCommandSuccessWithNoModelChange(paidCommand, model, expectedModel, commandHistory, expectedMessage);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        PaidCommand paidCommand = new PaidCommand(outOfBoundIndex);

        ModelManager expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());

        assertCommandFailureWithNoModelChange(paidCommand, model, expectedModel, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void execute_validIndexFilteredList_success() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Transaction personWhoPaid = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_TRANSACTION);

        String expectedMessage = String.format(PaidCommand.MESSAGE_PAID_TRANSACTION_SUCCESS, personWhoPaid);

        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.deleteTransaction(personWhoPaid);
        expectedModel.addPastTransaction(personWhoPaid);
        expectedModel.commitFinancialDatabase();
        showNoPerson(expectedModel);

        assertCommandSuccessWithNoModelChange(paidCommand, model, expectedModel, commandHistory, expectedMessage);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Index outOfBoundIndex = INDEX_SECOND_TRANSACTION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFinancialDatabase().getTransactionList().size());

        PaidCommand paidCommand = new PaidCommand(outOfBoundIndex);

        assertCommandFailureWithModelChange(paidCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Transaction personWhoPaid = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_TRANSACTION);
        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());
        expectedModel.deleteTransaction(personWhoPaid);
        expectedModel.addPastTransaction(personWhoPaid);
        expectedModel.commitFinancialDatabase();

        // delete -> first transaction deleted
        paidCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, expectedModel, commandHistory,
                UndoCommand.MESSAGE_SUCCESS);

        // redo -> same first transaction deleted again
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, expectedModel, commandHistory,
                RedoCommand.MESSAGE_SUCCESS);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        PaidCommand paidCommand = new PaidCommand(outOfBoundIndex);

        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());

        // execution failed -> financial database state not added into model
        assertCommandFailureWithNoModelChange(paidCommand, model, expectedModel, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single financial database state in model -> undoCommand and redoCommand fail
        assertCommandFailureWithNoModelChange(new UndoCommand(), model, expectedModel, commandHistory,
                UndoCommand.MESSAGE_FAILURE);
        assertCommandFailureWithNoModelChange(new RedoCommand(), model, expectedModel, commandHistory,
                RedoCommand.MESSAGE_FAILURE);
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
        PaidCommand paidCommand = new PaidCommand(INDEX_FIRST_TRANSACTION);
        Model expectedModel = new ModelManager(model.getFinancialDatabase(), new UserPrefs());

        showTransactionAtIndex(model, INDEX_SECOND_TRANSACTION);
        Transaction personWhoPaid = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        expectedModel.deleteTransaction(personWhoPaid);
        expectedModel.addPastTransaction(personWhoPaid);
        expectedModel.commitFinancialDatabase();

        // delete -> deletes second transaction in unfiltered transaction list/ first
        // transaction in filtered transaction list
        paidCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, expectedModel, commandHistory,
                UndoCommand.MESSAGE_SUCCESS);

        assertNotEquals(personWhoPaid, model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased()));
        // redo -> deletes same second transaction in unfiltered transaction list
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, expectedModel, commandHistory,
                RedoCommand.MESSAGE_SUCCESS);
    }

    @Test
    public void equals() {
        PaidCommand paidFirstCommand = new PaidCommand(INDEX_FIRST_TRANSACTION);
        PaidCommand paidSecondCommand = new PaidCommand(INDEX_SECOND_TRANSACTION);

        // same object -> returns true
        assertTrue(paidFirstCommand.equals(paidFirstCommand));

        // same values -> returns true
        PaidCommand paidFirstCommandCopy = new PaidCommand(INDEX_FIRST_TRANSACTION);
        assertTrue(paidFirstCommand.equals(paidFirstCommandCopy));

        // different types -> returns false
        assertFalse(paidFirstCommand.equals(1));

        // null -> returns false
        assertFalse(paidFirstCommand.equals(null));

        // different transaction -> returns false
        assertFalse(paidFirstCommand.equals(paidSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredTransactionList(p -> false);

        assertTrue(model.getFilteredTransactionList().isEmpty());
    }
}
