package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalTransactions.ALICE_TRANSACTION;
import static seedu.address.ui.BrowserPanel.CALENDAR_PAGE_URL;
import static seedu.address.ui.BrowserPanel.DEFAULT_PAGE;
import static seedu.address.ui.UiPart.FXML_FILE_FOLDER;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import seedu.address.MainApp;
import seedu.address.commons.events.ui.ClearBrowserPanelEvent;
import seedu.address.commons.events.ui.RefreshCalendarEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.commons.events.ui.TransactionPanelSelectionChangedEvent;

public class BrowserPanelTest extends GuiUnitTest {
    private TransactionPanelSelectionChangedEvent selectionChangedEventStub;

    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new TransactionPanelSelectionChangedEvent(ALICE_TRANSACTION);

        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a transaction
        postNow(selectionChangedEventStub);
        URL expectedPersonUrl = new URL(BrowserPanel.SEARCH_PAGE_URL + ALICE_TRANSACTION.getDeadline().value
                .replaceAll(" ", "%20"));

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedPersonUrl, browserPanelHandle.getLoadedUrl());

        String calendarId = "test";
        URL expectedCalendarUrl = new URL(CALENDAR_PAGE_URL + calendarId + "&ctz=Asia/Singapore");

        ShowCalendarEvent showCalendarEvent = new ShowCalendarEvent(calendarId);
        postNow(showCalendarEvent);
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedCalendarUrl, browserPanelHandle.getLoadedUrl());

        RefreshCalendarEvent refreshCalendarEvent = new RefreshCalendarEvent(calendarId);
        postNow(refreshCalendarEvent);
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedCalendarUrl, browserPanelHandle.getLoadedUrl());

        ClearBrowserPanelEvent clearBrowserEvent = new ClearBrowserPanelEvent();
        postNow(clearBrowserEvent);
        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());
    }


}
