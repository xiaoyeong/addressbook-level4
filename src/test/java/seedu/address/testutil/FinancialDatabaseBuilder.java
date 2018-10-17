package seedu.address.testutil;

import seedu.address.model.FinancialDatabase;
import seedu.address.model.transaction.Transaction;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class FinancialDatabaseBuilder {

    private FinancialDatabase financialDatabase;

    public FinancialDatabaseBuilder() {
        financialDatabase = new FinancialDatabase();
    }

    public FinancialDatabaseBuilder(FinancialDatabase addressBook) {
        this.financialDatabase = addressBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     * @param transaction
     */
    public FinancialDatabaseBuilder withTransaction(Transaction transaction) {
        financialDatabase.addTransaction(transaction);
        return this;
    }

    public FinancialDatabase build() {
        return financialDatabase;
    }
}
