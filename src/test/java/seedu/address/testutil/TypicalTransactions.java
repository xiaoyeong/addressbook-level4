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
    public static final Transaction ALICE_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.ALICE)
            .withAmount("SGD 42.50")
            .withType("Loan").build();
    public static final Transaction BENSON_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.BENSON)
            .withAmount("AUD 12.85")
            .withType("Debt").build();
    public static final Transaction CARL_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.CARL)
            .withAmount("USD 57.60")
            .withType("Debt").build();
    public static final Transaction DANIEL_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.DANIEL)
            .withAmount("INR 44.70")
            .withType("Loan").build();
    public static final Transaction ELLE_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.ELLE)
            .withAmount("CNY 47.65")
            .withType("Debt").build();
    public static final Transaction FIONA_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.FIONA)
            .withAmount("MYR 558.65")
            .withType("Debt").build();
    public static final Transaction GEORGE_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.GEORGE)
            .withAmount("GBP 47.15")
            .withType("Debt").build();

    // Manually added
    public static final Transaction HOON_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.HOON)
            .withAmount("CNY 77.15")
            .withType("Debt").build();
    public static final Transaction IDA_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.IDA)
            .withAmount("CNY 67.15")
            .withType("Debt").build();
    public static final Transaction JACK_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.JACK)
            .withAmount("SGD 42.50")
            .withType("Debt")
            .withDeadline("19/11/2018").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Transaction AMY_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.AMY)
            .withAmount("SGD 145.60")
            .withType("Loan")
            .withDeadline("17/11/2018").build();
    public static final Transaction BOB_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.BOB)
            .withAmount("SGD 42.50")
            .withType("Loan")
            .withDeadline("12/11/2018").build();


    private TypicalTransactions() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static FinancialDatabase getTypicalFinancialDatabase() {
        FinancialDatabase database = new FinancialDatabase();
        for (Transaction transaction : getTypicalTransactions()) {
            database.addTransaction(transaction, database.getCurrentList());
        }
        return database;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(Arrays.asList(ALICE_TRANSACTION, BENSON_TRANSACTION, CARL_TRANSACTION,
                DANIEL_TRANSACTION, ELLE_TRANSACTION, FIONA_TRANSACTION, GEORGE_TRANSACTION));
    }

    /**
     * Returns an {@code AddressBook} with all the unique persons.
     */
    public static FinancialDatabase getUniqueFinancialDatabase() {
        FinancialDatabase database = new FinancialDatabase();
        for (Transaction transaction : getUniqueTransactions()) {
            database.addTransaction(transaction, database.getCurrentList());
        }
        return database;
    }
    public static List<Transaction> getUniqueTransactions() {
        return new ArrayList<>(Arrays.asList(ALICE_TRANSACTION, AMY_TRANSACTION, BOB_TRANSACTION,
                 JACK_TRANSACTION));
    }
}
