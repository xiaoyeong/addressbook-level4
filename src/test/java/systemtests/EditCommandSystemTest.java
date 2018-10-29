package systemtests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TRANSACTION;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalTransactions.AMY_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.BOB_TRANSACTION;

import java.util.logging.Logger;

import org.junit.Test;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.TransactionBuilder;
import seedu.address.testutil.TransactionUtil;

public class EditCommandSystemTest extends FinancialDatabaseSystemTest {

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    @Test
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_TRANSACTION;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PHONE_DESC_BOB + " " + EMAIL_DESC_BOB + "  " + ADDRESS_DESC_BOB + "  " + AMOUNT_DESC_BOB + "  "
                + TYPE_DESC_BOB + "  " + DEADLINE_DESC_BOB + "  " + TAG_DESC_HUSBAND + " ";
        Transaction editedTransaction = new TransactionBuilder(BOB_TRANSACTION).withTags(VALID_TAG_HUSBAND).build();
        logger.info("" + index.getZeroBased());
        assertCommandSuccess(command, index, editedTransaction);

        /* Case: undo editing the last transaction in the list -> last transaction restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last transaction in the list -> last transaction edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateTransaction(
                getModel().getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased()), editedTransaction);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a transaction with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + AMOUNT_DESC_BOB + TYPE_DESC_BOB + DEADLINE_DESC_BOB + TAG_DESC_FRIEND
                + TAG_DESC_HUSBAND;
        assertCommandSuccess(command, index, BOB_TRANSACTION);

        /* Case: edit a transaction with new values same as another transaction's values but with different name ->
         * edited
         */
        assertTrue(getModel().getFinancialDatabase().getTransactionList().contains(BOB_TRANSACTION));
        index = INDEX_SECOND_TRANSACTION;
        assertNotEquals(getModel().getFilteredTransactionList().get(index.getZeroBased()), BOB);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + AMOUNT_DESC_BOB + TYPE_DESC_BOB + DEADLINE_DESC_BOB + TAG_DESC_FRIEND
                + TAG_DESC_HUSBAND;
        editedTransaction = new TransactionBuilder(BOB_TRANSACTION).withName(VALID_NAME_AMY).build();
        assertCommandSuccess(command, index, editedTransaction);

        /* Case: edit a transaction with new values same as another transaction's values but with different phone and
         * email -> edited
         */
        index = INDEX_SECOND_TRANSACTION;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;
        editedTransaction = new TransactionBuilder(BOB_TRANSACTION).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY)
                .build();
        assertCommandSuccess(command, index, editedTransaction);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_TRANSACTION;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Transaction personToEdit = getModel().getFilteredTransactionList().get(index.getZeroBased());
        editedTransaction = new TransactionBuilder(personToEdit).withTags().build();
        assertCommandSuccess(command, index, editedTransaction);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered transaction list, edit index within bounds of address book and transaction list -> edited */
        showTransactionsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_TRANSACTION;
        assertTrue(index.getZeroBased() < getModel().getFilteredTransactionList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        personToEdit = getModel().getFilteredTransactionList().get(index.getZeroBased());
        editedTransaction = new TransactionBuilder(personToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedTransaction);

        /* Case: filtered transaction list, edit index within bounds of address book but out of bounds of transaction
         * list -> rejected
         */
        showTransactionsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getFinancialDatabase().getTransactionList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a transaction card is selected --------------------- */

        /* Case: selects first card in the transaction list, edit a transaction -> edited, card selection remains
         * unchanged but browser url changes.
         */
        showAllTransactions();
        index = INDEX_FIRST_TRANSACTION;
        selectTransaction(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + AMOUNT_DESC_AMY + TYPE_DESC_AMY + DEADLINE_DESC_AMY + TAG_DESC_FRIEND;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new transaction's name
        assertCommandSuccess(command, index, AMY_TRANSACTION, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredTransactionList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased()
                        + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased()
                        + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased()
                        + INVALID_EMAIL_DESC, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased()
                        + INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased()
                        + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: edit a transaction with new values same as another transaction's values -> rejected */
        executeCommand(TransactionUtil.getAddCommand(BOB_TRANSACTION));
        assertTrue(getModel().getFinancialDatabase().getTransactionList().contains(BOB_TRANSACTION));
        index = INDEX_FIRST_TRANSACTION;
        assertNotEquals(getModel().getFilteredTransactionList().get(index.getZeroBased()), BOB_TRANSACTION);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + AMOUNT_DESC_BOB + TYPE_DESC_BOB + DEADLINE_DESC_BOB + TAG_DESC_FRIEND
                + TAG_DESC_HUSBAND;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccessWithNoModelChange(String, Index, Person, Index)}
     * except that the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Transaction, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Transaction editedTransaction) {
        assertCommandSuccess(command, toEdit, editedTransaction, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccessWithNoModelChange(String, Model, String, Index)}
     * and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the transaction at index {@code toEdit} being
     * updated to values specified {@code editedTransaction}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Transaction editedTransaction,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.updateTransaction(expectedModel.getFilteredTransactionList().get(toEdit.getZeroBased()),
                editedTransaction);
        expectedModel.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_TRANSACTION_SUCCESS, editedTransaction),
                expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccessWithNoModelChange(String, Model, String, Index)}
     * except that the browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code FinancialDatabaseSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see FinancialDatabaseSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see FinancialDatabaseSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
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
