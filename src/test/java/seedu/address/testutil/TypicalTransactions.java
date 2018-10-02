package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.FinancialDatabase;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE_TRANSACTION = new TransactionBuilder().withPerson(TypicalPersons.ALICE);


    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Transaction transaction : getTypicalTransactions()) {
            ab.addPerson(transaction);
        }
        return ab;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<Transaction>(Arrays.asList(ALICE_TRANSACTION, BENSON_TRANSACTION, CARL_TRANSACTION,
                DANIEL_TRANSACTION, ELLE_TRANSACTION, FIONA_TRANSACTION, GEORGE_TRANSACTION));
    }
}
