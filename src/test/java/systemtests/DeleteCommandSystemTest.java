package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_TRANSACTION_SUCCESS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getTransaction;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

public class DeleteCommandSystemTest extends FinancialDatabaseSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);

    @Test
    public void delete() {
        /* ----------------- Performing delete operation while an unfiltered list is being shown -------------------- */

        /* Case: delete the first transaction in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command = "     " + DeleteCommand.COMMAND_WORD + "      " + INDEX_FIRST_TRANSACTION.getOneBased()
                + "       ";
        Transaction deletedTransaction = removeTransaction(expectedModel, INDEX_FIRST_TRANSACTION);
        String expectedResultMessage = String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, deletedTransaction);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last transaction in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastTransactionIndex = getLastIndex(modelBeforeDeletingLast);
        assertCommandSuccess(lastTransactionIndex);

        /* Case: undo deleting the last transaction in the list -> last transaction restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: redo deleting the last transaction in the list -> last transaction deleted again */
        command = RedoCommand.COMMAND_WORD;
        removeTransaction(modelBeforeDeletingLast, lastTransactionIndex);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle transaction in the list -> deleted */
        Index middleTransactionIndex = getMidIndex(getModel());
        assertCommandSuccess(middleTransactionIndex);

        /* ------------------ Performing delete operation while a filtered list is being shown ---------------------- */

        /* Case: filtered transaction list, delete index within bounds of address book and
         * transaction list -> deleted
         */
        showTransactionsWithName(KEYWORD_MATCHING_MEIER);
        Index index = INDEX_FIRST_TRANSACTION;
        assertTrue(index.getZeroBased() < getModel().getFilteredTransactionList().size());
        assertCommandSuccess(index);

        /* Case: filtered transaction list, delete index within bounds of address book but out of bounds of
         * transaction list
         * -> rejected
         */
        showTransactionsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getFinancialDatabase().getTransactionList().size();
        command = DeleteCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);

        /* --------------------- Performing delete operation while a transaction card is selected ------------------- */

        /* Case: delete the selected transaction -> transaction list panel selects the transaction before the deleted
         * transaction
         */
        showAllTransactions();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectTransaction(selectedIndex);
        command = DeleteCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedTransaction = removeTransaction(expectedModel, selectedIndex);
        expectedResultMessage = String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, deletedTransaction);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid delete operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getFinancialDatabase().getTransactionList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Transaction} at the specified {@code index} in {@code model}'s address book.
     * @return the removed transaction
     */
    private Transaction removeTransaction(Model model, Index index) {
        Transaction targetTransaction = getTransaction(model, index);
        model.deleteTransaction(targetTransaction);
        return targetTransaction;
    }

    /**
     * Deletes the transaction at {@code toDelete} by creating a default {@code DeleteCommand} using {@code toDelete}
     * and performs the same verification as {@code assertCommandSuccessWithNoModelChange(String, Model, String)}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) {
        Model expectedModel = getModel();
        Transaction deletedTransaction = removeTransaction(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, deletedTransaction);

        assertCommandSuccess(
                DeleteCommand.COMMAND_WORD + " " + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card remains unchanged.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code FinancialDatabaseSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see FinancialDatabaseSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccessWithNoModelChange(String, Model, String)} except
     * that the browser url and selected card are expected to update accordingly depending on the
     * card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see FinancialDatabaseSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code FinancialDatabaseSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see FinancialDatabaseSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
