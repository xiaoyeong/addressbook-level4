package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.transaction.Transaction;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class PhotoCommandTest {

    private Model model = new ModelManager(getTypicalFinancialDatabase(), new UserPrefs());

    @Test
    public void deletePhotoTest() throws IllegalValueException {
        Transaction transactionToAdd = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        Transaction testTransaction = new Transaction(transactionToAdd);
        testTransaction.setPhoto("delete");
        assertEquals(transactionToAdd, testTransaction);
    }


}
