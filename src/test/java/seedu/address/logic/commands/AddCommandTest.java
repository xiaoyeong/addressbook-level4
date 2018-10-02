package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.FinancialDatabase;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyFinancialDatabase;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.TransactionBuilder;
import seedu.address.testutil.TypicalPersons;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullTransaction_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_transactionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub = new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = new TransactionBuilder().build();

        CommandResult commandResult = new AddCommand(validTransaction).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validTransaction), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTransaction), modelStub.transactionsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateTransaction_throwsCommandException() throws Exception {
        Transaction validTransaction = new TransactionBuilder().build();
        AddCommand addCommand = new AddCommand(validTransaction);
        ModelStub modelStub = new ModelStubWithTransaction(validTransaction);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_TRANSACTION);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Transaction aliceTransaction = new TransactionBuilder().withPerson(TypicalPersons.ALICE).build();
        Transaction bobTransaction = new TransactionBuilder().withPerson(TypicalPersons.BOB).build();
        AddCommand addAliceCommand = new AddCommand(aliceTransaction);
        AddCommand addBobCommand = new AddCommand(bobTransaction);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(aliceTransaction);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different transaction -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addTransaction(Transaction transaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyFinancialDatabase newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyFinancialDatabase getFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTransaction(Transaction transaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTransaction(Transaction target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateTransaction(Transaction target, Transaction editedTransaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Transaction> getFilteredTransactionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single transaction.
     */
    private class ModelStubWithTransaction extends ModelStub {
        private final Transaction transaction;

        ModelStubWithTransaction(Transaction transaction) {
            requireNonNull(transaction);
            this.transaction = transaction;
        }

        @Override
        public boolean hasTransaction(Transaction transaction) {
            requireNonNull(transaction);
            return this.transaction.equals(transaction);
        }
    }

    /**
     * A Model stub that always accept the transaction being added.
     */
    private class ModelStubAcceptingTransactionAdded extends ModelStub {
        final ArrayList<Transaction> transactionsAdded = new ArrayList<>();

        @Override
        public boolean hasTransaction(Transaction transaction) {
            requireNonNull(transaction);
            return transactionsAdded.stream().anyMatch(transaction::equals);
        }

        @Override
        public void addTransaction(Transaction transaction) {
            requireNonNull(transaction);
            transactionsAdded.add(transaction);
        }

        @Override
        public void commitFinancialDatabase() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyFinancialDatabase getFinancialDatabase() {
            return new FinancialDatabase();
        }
    }

}
