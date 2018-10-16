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

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Transaction validPerson = new TransactionBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        Transaction validPerson = new TransactionBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_TRANSACTION);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Transaction alice = new TransactionBuilder().withName("Alice").build();
        Transaction bob = new TransactionBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
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
        public void resetData(ReadOnlyFinancialDatabase newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyFinancialDatabase getFinancialDatabase() {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void addTransaction(seedu.address.model.transaction.Transaction transaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasTransaction(seedu.address.model.transaction.Transaction transaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateTransaction(seedu.address.model.transaction.Transaction target, seedu.address.model.transaction.Transaction editedTransaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTransaction(seedu.address.model.transaction.Transaction target) {
            throw new AssertionError("This method should not be called.");
        }

        public ObservableList<seedu.address.model.transaction.Transaction> getFilteredTransactionList() {
            throw new AssertionError("This method should not be called.");
        }

        public void updateFilteredTransactionList(Predicate<seedu.address.model.transaction.Transaction> predicate) {
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
    private class ModelStubWithPerson extends ModelStub {
        private final Transaction person;

        ModelStubWithPerson(Transaction person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasTransaction(Transaction person) {
            requireNonNull(person);
            return this.person.equals(person);
        }
    }

    /**
     * A Model stub that always accept the transaction being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Transaction> personsAdded = new ArrayList<>();

        @Override
        public boolean hasTransaction(Transaction person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::equals);
        }

        @Override
        public void addTransaction(Transaction person) {
            requireNonNull(person);
            personsAdded.add(person);
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
