package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.FinancialDatabase;
import seedu.address.model.ReadOnlyFinancialDatabase;
import seedu.address.model.transaction.Transaction;

/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "financialdatabase")
public class XmlSerializableFinancialDatabase {

    public static final String MESSAGE_DUPLICATE_TRANSACTION = "Database contains duplicate transaction(s).";
    @XmlElement
    private List<XmlAdaptedTransaction> transactions;
    @XmlElement
    private List<XmlAdaptedTransaction> pastTransactions;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableFinancialDatabase() {
        transactions = new ArrayList<>();
        pastTransactions = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableFinancialDatabase(ReadOnlyFinancialDatabase src) {
        this();
        transactions.addAll(src.getTransactionList()
                               .stream()
                               .map(XmlAdaptedTransaction::new)
                               .collect(Collectors.toList()));
        pastTransactions.addAll(src.getPastTransactionList()
                                   .stream()
                                   .map(XmlAdaptedTransaction::new)
                                   .collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedTransaction}.
     */
    public FinancialDatabase toModelType() throws IllegalValueException {
        FinancialDatabase financialDatabase = new FinancialDatabase();

        for (XmlAdaptedTransaction t : transactions) {
            Transaction transaction = t.toModelType();
            if (financialDatabase.hasTransaction(transaction, financialDatabase.getCurrentList())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TRANSACTION);
            }
            financialDatabase.addTransaction(transaction, financialDatabase.getCurrentList());
        }

        for (XmlAdaptedTransaction t : pastTransactions) {
            Transaction pastTransaction = t.toModelType();
            if (financialDatabase.hasTransaction(pastTransaction, financialDatabase.getPastList())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TRANSACTION);
            }
            financialDatabase.addTransaction(pastTransaction, financialDatabase.getPastList());
        }
        return financialDatabase;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableFinancialDatabase)) {
            return false;
        }
        return transactions.equals(((XmlSerializableFinancialDatabase) other).transactions);
    }
}
