package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.HOON_TRANSACTION;
import static seedu.address.testutil.TypicalTransactions.IDA_TRANSACTION;

import static seedu.address.testutil.TypicalTransactions.getTypicalFinancialDatabase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.FinancialDatabase;
import seedu.address.model.ReadOnlyFinancialDatabase;

public class XmlFinancialDatabaseStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "XmlFinancialDatabaseStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readFinancialDatabase_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readFinancialDatabase(null);
    }

    private java.util.Optional<ReadOnlyFinancialDatabase> readFinancialDatabase(String filePath) throws Exception {
        return new XmlFinancialDatabaseStorage(Paths.get(filePath))
                .readFinancialDatabase(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readFinancialDatabase("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readFinancialDatabase("NotXmlFormatFinancialDatabase.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readFinancialDatabase_invalidTransactionFinancialDatabase_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readFinancialDatabase("invalidTransactionFinancialDatabase.xml");
    }

    @Test
    public void readFinancialDatabase_invalidAndValidTransactionFinancialDatabase_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readFinancialDatabase("invalidAndValidTransactionFinancialDatabase.xml");
    }

    @Test
    public void readAndSaveFinancialDatabase_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempAddressBook.xml");
        FinancialDatabase original = getTypicalFinancialDatabase();
        XmlFinancialDatabaseStorage xmlAddressBookStorage = new XmlFinancialDatabaseStorage(filePath);

        //Save in new file and read back
        xmlAddressBookStorage.saveFinancialDatabase(original, filePath);
        ReadOnlyFinancialDatabase readBack = xmlAddressBookStorage.readFinancialDatabase(filePath).get();
        assertEquals(original, new FinancialDatabase(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addTransaction(HOON_TRANSACTION, original.getCurrentList());
        original.removeTransaction(ALICE_TRANSACTION, original.getCurrentList());
        xmlAddressBookStorage.saveFinancialDatabase(original, filePath);
        readBack = xmlAddressBookStorage.readFinancialDatabase(filePath).get();
        assertEquals(original, new FinancialDatabase(readBack));

        //Save and read without specifying file path
        original.addTransaction(IDA_TRANSACTION, original.getCurrentList());
        xmlAddressBookStorage.saveFinancialDatabase(original); //file path not specified
        readBack = xmlAddressBookStorage.readFinancialDatabase().get(); //file path not specified
        assertEquals(original, new FinancialDatabase(readBack));

    }

    @Test
    public void saveFinancialDatabase_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveFinancialDatabase(null, "SomeFile.xml");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveFinancialDatabase(ReadOnlyFinancialDatabase addressBook, String filePath) {
        try {
            new XmlFinancialDatabaseStorage(Paths.get(filePath))
                    .saveFinancialDatabase(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveFinancialDatabase_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveFinancialDatabase(new FinancialDatabase(), null);
    }


}
