package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 *
 */
public class SwitchTabsEvent extends BaseEvent {
    public final int tabIndex;

    public SwitchTabsEvent(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
