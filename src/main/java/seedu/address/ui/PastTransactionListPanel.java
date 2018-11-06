package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.TransactionPanelSelectionChangedEvent;
import seedu.address.model.transaction.Transaction;

/**
 *
 */
public class PastTransactionListPanel extends UiPart<Region> {
    private static final String FXML = "PastTransactionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PastTransactionListPanel.class);

    @FXML
    private ListView<Transaction> pastTransactionListView;

    public PastTransactionListPanel(ObservableList<Transaction> transactionList) {
        super(FXML);
        setConnections(transactionList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Transaction> personList) {
        pastTransactionListView.setItems(personList);
        pastTransactionListView.setCellFactory(listView -> new PastTransactionListPanel.TransactionListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        pastTransactionListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in transaction list panel changed to : '" + newValue + "'");
                        raise(new TransactionPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.

    private void scrollTo(int index) {
        Platform.runLater(() -> {
            pastTransactionListView.scrollTo(index);
            pastTransactionListView.getSelectionModel().clearAndSelect(index);
        });
    }
     */

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class TransactionListViewCell extends ListCell<Transaction> {
        @Override
        protected void updateItem(Transaction transaction, boolean empty) {
            super.updateItem(transaction, empty);

            if (empty || transaction == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new TransactionCard(transaction, getIndex() + 1).getRoot());
            }
        }
    }


}
