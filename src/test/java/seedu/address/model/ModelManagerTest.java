package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.BOB_TRANSACTION;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.FinancialDatabaseBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasTransaction(null);
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasTransaction(ALICE_TRANSACTION));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addTransaction(ALICE_TRANSACTION);
        assertTrue(modelManager.hasTransaction(ALICE_TRANSACTION));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredTransactionList().remove(0);
    }

    @Test
    public void equals() {
        FinancialDatabase addressBook = new FinancialDatabaseBuilder().withTransaction(ALICE_TRANSACTION).withTransaction(BOB_TRANSACTION).build();
        FinancialDatabase differentAddressBook = new FinancialDatabase();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE_TRANSACTION.getPerson().getName().fullName.split("\\s+");
        modelManager.updateFilteredTransactionList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
