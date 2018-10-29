package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_BOB;
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
import seedu.address.logic.commands.EditCommand.EditTransactionDescriptor;
import seedu.address.model.FinancialDatabase;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.EditTransactionDescriptorBuilder;
import seedu.address.testutil.TransactionBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Transaction editedPerson = new TransactionBuilder().build();
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRANSACTION, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(model.getFilteredTransactionList().get(0), editedPerson);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithNoModelChange(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastTransaction = Index.fromOneBased(model.getFilteredTransactionList().size());
        Transaction lastTransaction = model.getFilteredTransactionList().get(indexLastTransaction.getZeroBased());


        TransactionBuilder transactionInList = new TransactionBuilder(lastTransaction);
        Transaction editedTransaction = transactionInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withAmount(VALID_AMOUNT_BOB).withType(VALID_TYPE_BOB).withDeadline(VALID_DEADLINE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withAmount(VALID_AMOUNT_BOB).withType(VALID_TYPE_BOB)
                .withDeadline(VALID_DEADLINE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastTransaction, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedTransaction);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(lastTransaction, editedTransaction);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithNoModelChange(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRANSACTION, new EditTransactionDescriptor());
        Transaction editedPerson = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithNoModelChange(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Transaction personInFilteredList = model.getFilteredTransactionList()
                .get(INDEX_FIRST_TRANSACTION.getZeroBased());
        Transaction editedPerson = new TransactionBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRANSACTION,
                new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(model.getFilteredTransactionList().get(0), editedPerson);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccessWithNoModelChange(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Transaction firstPerson = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_TRANSACTION, descriptor);

        assertCommandFailureWithNoModelChange(editCommand, model, commandHistory,
                EditCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        // edit transaction in filtered list into a duplicate in address book
        Transaction personInList = model.getFinancialDatabase().getTransactionList()
                .get(INDEX_SECOND_TRANSACTION.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRANSACTION,
                new EditTransactionDescriptorBuilder(personInList).build());

        assertCommandFailureWithNoModelChange(editCommand, model, commandHistory,
                EditCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailureWithNoModelChange(editCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);
        Index outOfBoundIndex = INDEX_SECOND_TRANSACTION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFinancialDatabase().getTransactionList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailureWithNoModelChange(editCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Transaction editedPerson = new TransactionBuilder().build();
        Transaction personToEdit = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRANSACTION, descriptor);
        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(personToEdit, editedPerson);
        expectedModel.commitFinancialDatabase();

        // edit -> first transaction edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first transaction edited again
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailureWithNoModelChange(editCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailureWithNoModelChange(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailureWithNoModelChange(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Person} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited transaction in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the transaction object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        Transaction editedPerson = new TransactionBuilder().build();
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_TRANSACTION, descriptor);
        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());

        showTransactionAtIndex(model, INDEX_SECOND_TRANSACTION);
        Transaction personToEdit = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        expectedModel.updateTransaction(personToEdit, editedPerson);
        expectedModel.commitFinancialDatabase();

        // edit -> edits second transaction in unfiltered transaction list / first transaction
        // in filtered transaction list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased()), personToEdit);
        // redo -> edits same second transaction in unfiltered transaction list
        expectedModel.redoFinancialDatabase();
        assertCommandSuccessWithNoModelChange(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_TRANSACTION, DESC_AMY);

        // same values -> returns true
        EditTransactionDescriptor copyDescriptor = new EditTransactionDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_TRANSACTION, copyDescriptor);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new EditCommand(INDEX_SECOND_TRANSACTION, DESC_AMY));

        // different descriptor -> returns false
        assertNotEquals(standardCommand, new EditCommand(INDEX_FIRST_TRANSACTION, DESC_BOB));
    }

}
