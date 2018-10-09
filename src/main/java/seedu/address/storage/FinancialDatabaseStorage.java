package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyFinancialDatabase;

/**
 * Represents a storage for {@link seedu.address.model.FinancialDatabase}.
 */
public interface FinancialDatabaseStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getFinancialDatabaseFilePath();

    /**
     * Returns AddressBook data as a {@link seedu.address.model.ReadOnlyFinancialDatabase}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyFinancialDatabase> readFinancialDatabase() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyFinancialDatabase> readFinancialDatabase(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyFinancialDatabase} to the storage.
     * @param financialDatabase cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase) throws IOException;

    /**
     * @see #saveFinancialDatabase(ReadOnlyFinancialDatabase)
     */
    void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase, Path filePath) throws IOException;

}
