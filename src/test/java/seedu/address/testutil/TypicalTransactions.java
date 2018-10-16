package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.FinancialDatabase;
import seedu.address.model.transaction.Transaction;

/**
 * A utility class containing a list of {@code Transaction} objects to be used in tests.
 */
public class TypicalTransactions {

    //TODO: Update according to accepted currencies
    public static final Transaction ALICE_TRANSACTION = new TransactionBuilder().withAmount("SGD 42.50")
            .withType("Loan").build();
    public static final Transaction BOB_TRANSACTION = new TransactionBuilder().withAmount("AUD 12.85")
            .withType("Debt").build();
    public static final Transaction CARL_TRANSACTION = new TransactionBuilder().withAmount("USD 57.60")
            .withType("Debt").build();
    public static final Transaction DANIEL_TRANSACTION = new TransactionBuilder().withAmount("INR 44.70")
            .withType("Loan").build();
    public static final Transaction ELLE_TRANSACTION = new TransactionBuilder().withAmount("CNY 47.65")
            .withType("Debt").build();

    private TypicalTransactions() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static FinancialDatabase getTypicalFinancialDatabase() {
        FinancialDatabase database = new FinancialDatabase();
        for (Transaction transaction : getTypicalTransactions()) {
            database.addTransaction(transaction);
        }
        return database;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(Arrays.asList(ALICE_TRANSACTION, BOB_TRANSACTION, CARL_TRANSACTION,
                DANIEL_TRANSACTION, ELLE_TRANSACTION));
    }
}
