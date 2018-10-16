package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_BOB;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TransactionBuilder;

public class FinancialDatabaseTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final FinancialDatabase financialDatabase = new FinancialDatabase();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), financialDatabase.getTransactionList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        financialDatabase.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyFinancialDatabase_replacesData() {
        FinancialDatabase newData = getTypicalFinancialDatabase();
        financialDatabase.resetData(newData);
        assertEquals(newData, financialDatabase);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person alice = new PersonBuilder().withName(VALID_NAME_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        Transaction editedAliceTransaction = new TransactionBuilder().withAmount(VALID_AMOUNT_BOB)
                .withPerson(alice).build();
        List<Transaction> newTransactions = Arrays.asList(ALICE_TRANSACTION, editedAliceTransaction);
        FinancialDatabaseStub newData = new FinancialDatabaseStub(newTransactions);

        thrown.expect(DuplicatePersonException.class);
        financialDatabase.resetData(newData);
    }

    @Test
    public void hasTransaction_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        financialDatabase.hasTransaction(null);
    }

    @Test
    public void hasTransaction_transactionNotInFinancialDatabase_returnsFalse() {
        assertFalse(financialDatabase.hasTransaction(ALICE_TRANSACTION));
    }

    @Test
    public void hasTransaction_transactionInFinancialDatabase_returnsTrue() {
        financialDatabase.addTransaction(ALICE_TRANSACTION);
        assertTrue(financialDatabase.hasTransaction(ALICE_TRANSACTION));
    }

    @Test
    public void hasTransaction_identicalTransactionInFinancialDatabase_returnsTrue() {
        financialDatabase.addTransaction(ALICE_TRANSACTION);
        Transaction editedAlice = new TransactionBuilder(ALICE_TRANSACTION)
                .withAmount(VALID_AMOUNT_BOB).withType(VALID_TYPE_BOB).withPerson(BOB)
                .build();
        assertTrue(financialDatabase.hasTransaction(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        financialDatabase.getTransactionList().remove(0);
    }

    /**
     * A stub ReadOnlyFinancialDatabase whose persons list can violate interface constraints.
     */
    private static class FinancialDatabaseStub implements ReadOnlyFinancialDatabase {
        private final ObservableList<Transaction> persons = FXCollections.observableArrayList();

        FinancialDatabaseStub(Collection<Transaction> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Transaction> getTransactionList() {
            return persons;
        }

    }

}
