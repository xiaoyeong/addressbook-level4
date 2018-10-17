package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.BOB_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.CARL_TRANSACTION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.FinancialDatabaseBuilder;

public class VersionedFinancialDatabaseTest {

    private final ReadOnlyFinancialDatabase addressBookWithAmy = new FinancialDatabaseBuilder().withTransaction(ALICE_TRANSACTION).build();
    private final ReadOnlyFinancialDatabase addressBookWithBob = new FinancialDatabaseBuilder().withTransaction(BOB_TRANSACTION).build();
    private final ReadOnlyFinancialDatabase addressBookWithCarl = new FinancialDatabaseBuilder().withTransaction(CARL_TRANSACTION).build();
    private final ReadOnlyFinancialDatabase emptyAddressBook = new FinancialDatabaseBuilder().build();

    @Test
    public void commit_singleAddressBook_noStatesRemovedCurrentStateSaved() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        VersionedFinancialDatabase.commit();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                emptyAddressBook,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        VersionedFinancialDatabase.commit();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Arrays.asList(emptyAddressBook, addressBookWithAmy, addressBookWithBob),
                addressBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 2);

        VersionedFinancialDatabase.commit();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                emptyAddressBook,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtEndOfStateList_returnsTrue() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertTrue(VersionedFinancialDatabase.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 1);

        assertTrue(VersionedFinancialDatabase.canUndo());
    }

    @Test
    public void canUndo_singleAddressBook_returnsFalse() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertFalse(VersionedFinancialDatabase.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsFalse() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 2);

        assertFalse(VersionedFinancialDatabase.canUndo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerNotAtEndOfStateList_returnsTrue() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 1);

        assertTrue(VersionedFinancialDatabase.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 2);

        assertTrue(VersionedFinancialDatabase.canRedo());
    }

    @Test
    public void canRedo_singleAddressBook_returnsFalse() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertFalse(VersionedFinancialDatabase.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtEndOfStateList_returnsFalse() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertFalse(VersionedFinancialDatabase.canRedo());
    }

    @Test
    public void undo_multipleAddressBookPointerAtEndOfStateList_success() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        VersionedFinancialDatabase.undo();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                addressBookWithAmy,
                Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void undo_multipleAddressBookPointerNotAtStartOfStateList_success() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 1);

        VersionedFinancialDatabase.undo();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Collections.emptyList(),
                emptyAddressBook,
                Arrays.asList(addressBookWithAmy, addressBookWithBob));
    }

    @Test
    public void undo_singleAddressBook_throwsNoUndoableStateException() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedFinancialDatabase.NoUndoableStateException.class, VersionedFinancialDatabase::undo);
    }

    @Test
    public void undo_multipleAddressBookPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 2);

        assertThrows(VersionedFinancialDatabase.NoUndoableStateException.class, VersionedFinancialDatabase::undo);
    }

    @Test
    public void redo_multipleAddressBookPointerNotAtEndOfStateList_success() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 1);

        VersionedFinancialDatabase.redo();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Arrays.asList(emptyAddressBook, addressBookWithAmy),
                addressBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleAddressBookPointerAtStartOfStateList_success() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 2);

        VersionedFinancialDatabase.redo();
        assertAddressBookListStatus(VersionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                addressBookWithAmy,
                Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void redo_singleAddressBook_throwsNoRedoableStateException() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedFinancialDatabase.NoRedoableStateException.class, VersionedFinancialDatabase::redo);
    }

    @Test
    public void redo_multipleAddressBookPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertThrows(VersionedFinancialDatabase.NoRedoableStateException.class, VersionedFinancialDatabase::redo);
    }

    @Test
    public void equals() {
        VersionedFinancialDatabase VersionedFinancialDatabase = prepareAddressBookList(addressBookWithAmy, addressBookWithBob);

        // same values -> returns true
        VersionedFinancialDatabase copy = prepareAddressBookList(addressBookWithAmy, addressBookWithBob);
        assertTrue(VersionedFinancialDatabase.equals(copy));

        // same object -> returns true
        assertTrue(VersionedFinancialDatabase.equals(VersionedFinancialDatabase));

        // null -> returns false
        assertFalse(VersionedFinancialDatabase.equals(null));

        // different types -> returns false
        assertFalse(VersionedFinancialDatabase.equals(1));

        // different state list -> returns false
        VersionedFinancialDatabase differentAddressBookList = prepareAddressBookList(addressBookWithBob, addressBookWithCarl);
        assertFalse(VersionedFinancialDatabase.equals(differentAddressBookList));

        // different current pointer index -> returns false
        VersionedFinancialDatabase differentCurrentStatePointer = prepareAddressBookList(
                addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase, 1);
        assertFalse(VersionedFinancialDatabase.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code VersionedFinancialDatabase} is currently pointing at {@code expectedCurrentState},
     * states before {@code VersionedFinancialDatabase#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code VersionedFinancialDatabase#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertAddressBookListStatus(VersionedFinancialDatabase VersionedFinancialDatabase,
                                             List<ReadOnlyFinancialDatabase> expectedStatesBeforePointer,
                                             ReadOnlyFinancialDatabase expectedCurrentState,
                                             List<ReadOnlyFinancialDatabase> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new FinancialDatabase(VersionedFinancialDatabase), expectedCurrentState);

        // shift pointer to start of state list
        while (VersionedFinancialDatabase.canUndo()) {
            VersionedFinancialDatabase.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyFinancialDatabase expectedAddressBook : expectedStatesBeforePointer) {
            assertEquals(expectedAddressBook, new FinancialDatabase(VersionedFinancialDatabase));
            VersionedFinancialDatabase.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyFinancialDatabase expectedAddressBook : expectedStatesAfterPointer) {
            VersionedFinancialDatabase.redo();
            assertEquals(expectedAddressBook, new FinancialDatabase(VersionedFinancialDatabase));
        }

        // check that there are no more states after pointer
        assertFalse(VersionedFinancialDatabase.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> VersionedFinancialDatabase.undo());
    }

    /**
     * Creates and returns a {@code VersionedFinancialDatabase} with the {@code addressBookStates} added into it, and the
     * {@code VersionedFinancialDatabase#currentStatePointer} at the end of list.
     */
    private VersionedFinancialDatabase prepareAddressBookList(ReadOnlyFinancialDatabase... addressBookStates) {
        assertFalse(addressBookStates.length == 0);

        VersionedFinancialDatabase VersionedFinancialDatabase = new VersionedFinancialDatabase(addressBookStates[0]);
        for (int i = 1; i < addressBookStates.length; i++) {
            VersionedFinancialDatabase.resetData(addressBookStates[i]);
            VersionedFinancialDatabase.commit();
        }

        return VersionedFinancialDatabase;
    }

    /**
     * Shifts the {@code VersionedFinancialDatabase#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase VersionedFinancialDatabase, int count) {
        for (int i = 0; i < count; i++) {
            VersionedFinancialDatabase.undo();
        }
    }
}
