package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.transaction.Transaction;

/**
 * Represents a selection change in the Person List Panel
 */
public class TransactionPanelSelectionChangedEvent extends BaseEvent {


    private final Transaction newSelection;

    public TransactionPanelSelectionChangedEvent(Transaction newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public Transaction getNewSelection() {
        return newSelection;
    }
}
