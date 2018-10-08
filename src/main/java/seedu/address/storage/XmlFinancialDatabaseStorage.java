package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyFinancialDatabase;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlFinancialDatabaseStorage implements FinancialDatabaseStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlFinancialDatabaseStorage.class);

    private Path filePath;

    public XmlFinancialDatabaseStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFinancialDatabaseFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyFinancialDatabase> readFinancialDatabase() throws DataConversionException, IOException {
        return readFinancialDatabase(filePath);
    }

    /**
     * Similar to {@link #readFinancialDatabase()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyFinancialDatabase> readFinancialDatabase(Path filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("AddressBook file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableFinancialDatabase xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase) throws IOException {
        saveFinancialDatabase(financialDatabase, filePath);
    }

    /**
     * Similar to {@link #saveFinancialDatabase(ReadOnlyFinancialDatabase)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase, Path filePath) throws IOException {
        requireNonNull(financialDatabase);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableFinancialDatabase(financialDatabase));
    }

}
