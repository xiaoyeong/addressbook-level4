package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code FinancialDatabase} that keeps track of its own history.
 */
public class VersionedFinancialDatabase extends FinancialDatabase {

    private final List<ReadOnlyFinancialDatabase> financialDatabaseStateList;
    private int currentStatePointer;

    public VersionedFinancialDatabase(ReadOnlyFinancialDatabase initialState) {
        super(initialState);

        financialDatabaseStateList = new ArrayList<>();
        financialDatabaseStateList.add(new FinancialDatabase(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code FinancialDatabase} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        financialDatabaseStateList.add(new FinancialDatabase(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        financialDatabaseStateList.subList(currentStatePointer + 1, financialDatabaseStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(financialDatabaseStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(financialDatabaseStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < financialDatabaseStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedFinancialDatabase)) {
            return false;
        }

        VersionedFinancialDatabase otherVersionedFinancialDatabase = (VersionedFinancialDatabase) other;

        // state check
        return super.equals(otherVersionedFinancialDatabase)
                && financialDatabaseStateList.equals(otherVersionedFinancialDatabase.financialDatabaseStateList)
                && currentStatePointer == otherVersionedFinancialDatabase.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of addressBookState list, unable to redo.");
        }
    }
}
