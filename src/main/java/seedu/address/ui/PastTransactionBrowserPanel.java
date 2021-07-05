package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.TransactionPanelSelectionChangedEvent;
import seedu.address.model.transaction.Transaction;

/**
 *  The Browser Panel of the past transactions list of the App.
 */
public class PastTransactionBrowserPanel extends UiPart<Region> {
    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";

    private static final String FXML = "PastTransactionBrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView pastTransactionBrowser;

    public PastTransactionBrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    private void loadTransactionPage(Transaction transaction) {
        loadPage(SEARCH_PAGE_URL + transaction.getDeadline().value);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> pastTransactionBrowser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        pastTransactionBrowser = null;
    }

    @Subscribe
    private void handleTransactionPanelSelectionChangedEvent(TransactionPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadTransactionPage(event.getNewSelection());
    }

}
