package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.events.model.FinancialDatabaseChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyFinancialDatabase;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends FinancialDatabaseStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getFinancialDatabaseFilePath();

    @Override
    Optional<ReadOnlyFinancialDatabase> readFinancialDatabase() throws DataConversionException, IOException;

    @Override
    void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleFinancialDatabaseChangedEvent(FinancialDatabaseChangedEvent abce);
}
