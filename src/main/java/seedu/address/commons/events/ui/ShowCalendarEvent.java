package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Represents a selection change in the Person List Panel
 */
public class ShowCalendarEvent extends BaseEvent {


    private final String calendarId;

    public ShowCalendarEvent(String calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public String getCalendarId() {
        return calendarId;
    }
}
