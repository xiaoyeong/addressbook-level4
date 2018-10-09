package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyFinancialDatabase;

/** Indicates the FinancialDatabase in the model has changed*/
public class FinancialDatabaseChangedEvent extends BaseEvent {

    public final ReadOnlyFinancialDatabase data;

    public FinancialDatabaseChangedEvent(ReadOnlyFinancialDatabase data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getPersonList().size();
    }
}
