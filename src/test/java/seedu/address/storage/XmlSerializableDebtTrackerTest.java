package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.FinancialDatabase;
import seedu.address.testutil.TypicalTransactions;

public class XmlSerializableDebtTrackerTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "XmlSerializableFinancialDatabaseTest");
    private static final Path TYPICAL_TRANSACTIONS_FILE =
            TEST_DATA_FOLDER.resolve("typicalTransactionsFinancialDatabase.xml");
    private static final Path INVALID_TRANSACTION_FILE =
            TEST_DATA_FOLDER.resolve("invalidTransactionFinancialDatabase.xml");
    private static final Path DUPLICATE_TRANSACTION_FILE =
            TEST_DATA_FOLDER.resolve("duplicateTransactionFinancialDatabase.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        XmlSerializableFinancialDatabase dataFromFile = XmlUtil.getDataFromFile(TYPICAL_TRANSACTIONS_FILE,
                XmlSerializableFinancialDatabase.class);
        FinancialDatabase financialDatabaseFromFile = dataFromFile.toModelType();
        FinancialDatabase typicalPersonsFinancialDatabase = TypicalTransactions.getTypicalFinancialDatabase();
        assertEquals(financialDatabaseFromFile, typicalPersonsFinancialDatabase);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        XmlSerializableFinancialDatabase dataFromFile = XmlUtil.getDataFromFile(INVALID_TRANSACTION_FILE,
                XmlSerializableFinancialDatabase.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        XmlSerializableFinancialDatabase dataFromFile = XmlUtil.getDataFromFile(DUPLICATE_TRANSACTION_FILE,
                XmlSerializableFinancialDatabase.class);
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlSerializableFinancialDatabase.MESSAGE_DUPLICATE_PERSON);
        dataFromFile.toModelType();
    }

}
