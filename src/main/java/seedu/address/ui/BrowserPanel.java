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
import seedu.address.commons.events.ui.RefreshCalendarEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.commons.events.ui.TransactionPanelSelectionChangedEvent;
import seedu.address.model.transaction.Transaction;


/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";
    public static final String CALENDAR_PAGE_URL =
            "https://calendar.google.com/calendar/b/1/embed?src=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private boolean calendarShown;

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    private void loadTransactionPage(Transaction transaction) {
        loadPage(SEARCH_PAGE_URL + transaction.getDeadline().value);
        calendarShown = false;
    }

    private void loadCalendarPage(String id) {
        loadPage(CALENDAR_PAGE_URL + id + "&ctz=Asia/Singapore");
        calendarShown = true;
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
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
        browser = null;
    }

    @Subscribe
    private void handleTransactionPanelSelectionChangedEvent(TransactionPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadTransactionPage(event.getNewSelection());
    }

    @Subscribe
    private void handleShowCalendarEvent(ShowCalendarEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadCalendarPage(event.getCalendarId());
    }

    @Subscribe
    private void handleRefreshCalendarEvent(RefreshCalendarEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (calendarShown) {
            loadCalendarPage(event.getCalendarId());
        }
    }
}
