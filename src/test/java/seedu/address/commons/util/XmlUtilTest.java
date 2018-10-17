package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.FinancialDatabase;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlAdaptedTransaction;
import seedu.address.storage.XmlSerializableFinancialDatabase;
import seedu.address.testutil.FinancialDatabaseBuilder;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TransactionBuilder;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validFinancialDatabase.xml");
    private static final Path MISSING_TRANSACTION_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingTransactionField.xml");
    private static final Path INVALID_TRANSACTION_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidTransactionField.xml");
    private static final Path VALID_TRANSACTION_FILE = TEST_DATA_FOLDER.resolve("validTransaction.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempFinancialDatabase.xml");

    private static final String INVALID_AMOUNT = "342.60";

    private static final String VALID_TYPE = "Loan";
    private static final String VALID_AMOUNT = "SGD 25.50";
    private static final String VALID_DEADLINE = "16/11/2018";
    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "9482424";
    private static final String VALID_EMAIL = "hans@example";
    private static final String VALID_ADDRESS = "4th street";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, FinancialDatabase.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, FinancialDatabase.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, FinancialDatabase.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        FinancialDatabase dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableFinancialDatabase.class).toModelType();
        assertEquals(9, dataFromFile.getTransactionList().size());
    }

    @Test
    public void xmlAdaptedTransactionFromFile_fileWithMissingTransactionField_validResult() throws Exception {
        XmlAdaptedTransaction actualTransaction = XmlUtil.getDataFromFile(
                MISSING_TRANSACTION_FIELD_FILE, XmlAdaptedTransactionWithRootElement.class);
        XmlAdaptedTransaction expectedTransaction = new XmlAdaptedTransaction(
                VALID_AMOUNT, null, VALID_DEADLINE, VALID_NAME, VALID_EMAIL, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedTransaction, actualTransaction);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithInvalidTransactionField_validResult() throws Exception {
        XmlAdaptedTransaction actualTransaction = XmlUtil.getDataFromFile(
                INVALID_TRANSACTION_FIELD_FILE, XmlAdaptedTransactionWithRootElement.class);
        XmlAdaptedTransaction expectedTransaction = new XmlAdaptedTransaction(
                INVALID_AMOUNT, VALID_TYPE, VALID_DEADLINE, VALID_NAME, VALID_EMAIL, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedTransaction, actualTransaction);
    }

    @Test
    public void xmlAdaptedPersonFromFile_fileWithValidTransaction_validResult() throws Exception {
        XmlAdaptedTransaction actualTransaction = XmlUtil.getDataFromFile(
                VALID_TRANSACTION_FILE, XmlAdaptedTransactionWithRootElement.class);
        XmlAdaptedTransaction expectedTransaction = new XmlAdaptedTransaction(
                VALID_TYPE, VALID_AMOUNT, VALID_DEADLINE, VALID_NAME, VALID_EMAIL, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS);
        assertEquals(expectedTransaction, actualTransaction);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new FinancialDatabase());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new FinancialDatabase());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableFinancialDatabase dataToWrite = new XmlSerializableFinancialDatabase(new FinancialDatabase());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableFinancialDatabase dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableFinancialDatabase.class);
        assertEquals(dataToWrite, dataFromFile);

        FinancialDatabaseBuilder builder = new FinancialDatabaseBuilder(new FinancialDatabase());
        dataToWrite = new XmlSerializableFinancialDatabase(
                builder.withTransaction(new TransactionBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableFinancialDatabase.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to
     * {@code XmlAdaptedTransaction} objects.
     */
    @XmlRootElement(name = "transaction")
    private static class XmlAdaptedTransactionWithRootElement extends XmlAdaptedTransaction {}
}
