package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.BOB_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.CARL_TRANSACTION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.FinancialDatabaseBuilder;

public class VersionedFinancialDatabaseTest {

    private final ReadOnlyFinancialDatabase addressBookWithAmy = new FinancialDatabaseBuilder()
            .withTransaction(ALICE_TRANSACTION)
            .build();
    private final ReadOnlyFinancialDatabase addressBookWithBob = new FinancialDatabaseBuilder()
            .withTransaction(BOB_TRANSACTION)
            .build();
    private final ReadOnlyFinancialDatabase addressBookWithCarl = new FinancialDatabaseBuilder()
            .withTransaction(CARL_TRANSACTION)
            .build();
    private final ReadOnlyFinancialDatabase emptyAddressBook = new FinancialDatabaseBuilder().build();

    @Test
    public void commit_singleAddressBook_noStatesRemovedCurrentStateSaved() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        versionedFinancialDatabase.commit();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                emptyAddressBook,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        versionedFinancialDatabase.commit();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Arrays.asList(emptyAddressBook, addressBookWithAmy, addressBookWithBob),
                addressBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 2);

        versionedFinancialDatabase.commit();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                emptyAddressBook,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtEndOfStateList_returnsTrue() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertTrue(versionedFinancialDatabase.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 1);

        assertTrue(versionedFinancialDatabase.canUndo());
    }

    @Test
    public void canUndo_singleAddressBook_returnsFalse() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertFalse(versionedFinancialDatabase.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsFalse() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 2);

        assertFalse(versionedFinancialDatabase.canUndo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerNotAtEndOfStateList_returnsTrue() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 1);

        assertTrue(versionedFinancialDatabase.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 2);

        assertTrue(versionedFinancialDatabase.canRedo());
    }

    @Test
    public void canRedo_singleAddressBook_returnsFalse() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertFalse(versionedFinancialDatabase.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtEndOfStateList_returnsFalse() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertFalse(versionedFinancialDatabase.canRedo());
    }

    @Test
    public void undo_multipleAddressBookPointerAtEndOfStateList_success() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        versionedFinancialDatabase.undo();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                addressBookWithAmy,
                Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void undo_multipleAddressBookPointerNotAtStartOfStateList_success() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 1);

        versionedFinancialDatabase.undo();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Collections.emptyList(),
                emptyAddressBook,
                Arrays.asList(addressBookWithAmy, addressBookWithBob));
    }

    @Test
    public void undo_singleAddressBook_throwsNoUndoableStateException() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedFinancialDatabase.NoUndoableStateException.class, versionedFinancialDatabase::undo);
    }

    @Test
    public void undo_multipleAddressBookPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 2);

        assertThrows(VersionedFinancialDatabase.NoUndoableStateException.class, versionedFinancialDatabase::undo);
    }

    @Test
    public void redo_multipleAddressBookPointerNotAtEndOfStateList_success() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 1);

        versionedFinancialDatabase.redo();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Arrays.asList(emptyAddressBook, addressBookWithAmy),
                addressBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleAddressBookPointerAtStartOfStateList_success() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 2);

        versionedFinancialDatabase.redo();
        assertAddressBookListStatus(versionedFinancialDatabase,
                Collections.singletonList(emptyAddressBook),
                addressBookWithAmy,
                Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void redo_singleAddressBook_throwsNoRedoableStateException() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedFinancialDatabase.NoRedoableStateException.class, versionedFinancialDatabase::redo);
    }

    @Test
    public void redo_multipleAddressBookPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertThrows(VersionedFinancialDatabase.NoRedoableStateException.class, versionedFinancialDatabase::redo);
    }

    @Test
    public void equals() {
        VersionedFinancialDatabase versionedFinancialDatabase = prepareAddressBookList(addressBookWithAmy,
                addressBookWithBob);

        // same values -> returns true
        VersionedFinancialDatabase copy = prepareAddressBookList(addressBookWithAmy, addressBookWithBob);
        assertTrue(versionedFinancialDatabase.equals(copy));

        // same object -> returns true
        assertTrue(versionedFinancialDatabase.equals(versionedFinancialDatabase));

        // null -> returns false
        assertFalse(versionedFinancialDatabase.equals(null));

        // different types -> returns false
        assertFalse(versionedFinancialDatabase.equals(1));

        // different state list -> returns false
        VersionedFinancialDatabase differentAddressBookList = prepareAddressBookList(addressBookWithBob,
                addressBookWithCarl);
        assertFalse(versionedFinancialDatabase.equals(differentAddressBookList));

        // different current pointer index -> returns false
        VersionedFinancialDatabase differentCurrentStatePointer = prepareAddressBookList(
                addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedFinancialDatabase, 1);
        assertFalse(versionedFinancialDatabase.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code VersionedFinancialDatabase} is currently pointing at {@code expectedCurrentState},
     * states before {@code VersionedFinancialDatabase#currentStatePointer} is equal to
     * {@code expectedStatesBeforePointer}, and states after {@code VersionedFinancialDatabase#currentStatePointer}
     * is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertAddressBookListStatus(VersionedFinancialDatabase versionedFinancialDatabase,
                                             List<ReadOnlyFinancialDatabase> expectedStatesBeforePointer,
                                             ReadOnlyFinancialDatabase expectedCurrentState,
                                             List<ReadOnlyFinancialDatabase> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new FinancialDatabase(versionedFinancialDatabase), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedFinancialDatabase.canUndo()) {
            versionedFinancialDatabase.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyFinancialDatabase expectedAddressBook : expectedStatesBeforePointer) {
            assertEquals(expectedAddressBook, new FinancialDatabase(versionedFinancialDatabase));
            versionedFinancialDatabase.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyFinancialDatabase expectedAddressBook : expectedStatesAfterPointer) {
            versionedFinancialDatabase.redo();
            assertEquals(expectedAddressBook, new FinancialDatabase(versionedFinancialDatabase));
        }

        // check that there are no more states after pointer
        assertFalse(versionedFinancialDatabase.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedFinancialDatabase.undo());
    }

    /**
     * Creates and returns a {@code VersionedFinancialDatabase} with the {@code addressBookStates} added into it,
     * and the {@code VersionedFinancialDatabase#currentStatePointer} at the end of list.
     */
    private VersionedFinancialDatabase prepareAddressBookList(ReadOnlyFinancialDatabase... addressBookStates) {
        assertFalse(addressBookStates.length == 0);

        VersionedFinancialDatabase versionedFinancialDatabase = new VersionedFinancialDatabase(addressBookStates[0]);
        for (int i = 1; i < addressBookStates.length; i++) {
            versionedFinancialDatabase.resetData(addressBookStates[i]);
            versionedFinancialDatabase.commit();
        }

        return versionedFinancialDatabase;
    }

    /**
     * Shifts the {@code VersionedFinancialDatabase#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedFinancialDatabase versionedFinancialDatabase, int count) {
        for (int i = 0; i < count; i++) {
            versionedFinancialDatabase.undo();
        }
    }
}
