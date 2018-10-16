package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
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
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(model.getFilteredTransactionList().get(0), editedPerson);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredTransactionList().size());
        Transaction lastPerson = model.getFilteredTransactionList().get(indexLastPerson.getZeroBased());


        TransactionBuilder personInList = new TransactionBuilder(lastPerson);
        Transaction editedPerson = personInList.withAmount(VALID_AMOUNT_AMY).withType(VALID_TYPE_AMY).build();

        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(lastPerson, editedPerson);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditTransactionDescriptor());
        Transaction editedPerson = model.getFilteredTransactionList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.commitFinancialDatabase();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showTransactionAtIndex(model, INDEX_FIRST_PERSON);

        Transaction personInFilteredList = model.getFilteredTransactionList().get(INDEX_FIRST_PERSON.getZeroBased());
        Transaction editedPerson = new TransactionBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(model.getFilteredTransactionList().get(0), editedPerson);
        expectedModel.commitFinancialDatabase();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Transaction firstPerson = model.getFilteredTransactionList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showTransactionAtIndex(model, INDEX_FIRST_PERSON);

        // edit transaction in filtered list into a duplicate in address book
        Transaction personInList = model.getFinancialDatabase().getTransactionList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditTransactionDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showTransactionAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFinancialDatabase().getTransactionList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Transaction editedPerson = new TransactionBuilder().build();
        Transaction personToEdit = model.getFilteredTransactionList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());
        expectedModel.updateTransaction(personToEdit, editedPerson);
        expectedModel.commitFinancialDatabase();

        // edit -> first transaction edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first transaction edited again
        expectedModel.redoFinancialDatabase();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
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
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new FinancialDatabase(model.getFinancialDatabase()), new UserPrefs());

        showTransactionAtIndex(model, INDEX_SECOND_PERSON);
        Transaction personToEdit = model.getFilteredTransactionList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.updateTransaction(personToEdit, editedPerson);
        expectedModel.commitFinancialDatabase();

        // edit -> edits second transaction in unfiltered transaction list / first transaction in filtered transaction list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered transaction list to show all persons
        expectedModel.undoFinancialDatabase();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredTransactionList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);
        // redo -> edits same second transaction in unfiltered transaction list
        expectedModel.redoFinancialDatabase();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditTransactionDescriptor copyDescriptor = new EditTransactionDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

}
