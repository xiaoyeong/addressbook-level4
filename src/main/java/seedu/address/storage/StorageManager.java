package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.FinancialDatabaseChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyFinancialDatabase;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private FinancialDatabaseStorage financialDatabaseStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(FinancialDatabaseStorage financialDatabaseStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.financialDatabaseStorage = financialDatabaseStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ Financial Database methods ==============================

    @Override
    public Path getFinancialDatabaseFilePath() {
        return financialDatabaseStorage.getFinancialDatabaseFilePath();
    }

    @Override
    public Optional<ReadOnlyFinancialDatabase> readFinancialDatabase() throws DataConversionException, IOException {
        return readFinancialDatabase(financialDatabaseStorage.getFinancialDatabaseFilePath());
    }

    @Override
    public Optional<ReadOnlyFinancialDatabase> readFinancialDatabase(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return financialDatabaseStorage.readFinancialDatabase(filePath);
    }

    @Override
    public void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase) throws IOException {
        saveFinancialDatabase(financialDatabase, financialDatabaseStorage.getFinancialDatabaseFilePath());
    }

    @Override
    public void saveFinancialDatabase(ReadOnlyFinancialDatabase financialDatabase, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        financialDatabaseStorage.saveFinancialDatabase(financialDatabase, filePath);
    }


    @Override
    @Subscribe
    public void handleFinancialDatabaseChangedEvent(FinancialDatabaseChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveFinancialDatabase(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
