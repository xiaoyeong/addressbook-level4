package seedu.address.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class TransactionCard extends UiPart<Region> {
    private static final String DEFAULT_IMAGE = "lejolly.png";
    private static final String FXML = "TransactionListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Transaction transaction;
    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label type;
    @FXML
    private Label amount;
    @FXML
    private Label deadline;
    @FXML
    private Label name;
    @FXML
    private Label email;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView imageView;

    public TransactionCard(Transaction transaction, int displayedIndex) {
        super(FXML);
        this.transaction = transaction;
        Person person = transaction.getPerson();
        id.setText(displayedIndex + ". ");
        type.setText(transaction.getType().value);
        amount.setText(transaction.getAmount().toString());
        deadline.setText(transaction.getDeadline().value);
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        String link = transaction.getPhoto().getPicturePath().trim();
        logger.info("before fail");
        logger.info(link);
        addPicture(transaction);
    }

    /**
     * Adds a picture for each transaction.
     */
    public void addPicture(Transaction transaction) {

        String currentpath = System.getProperty("user.dir");
        logger.info("current path is:" + currentpath);
        try {
            String url = transaction.getPhoto().getPicturePath();

            if (url.startsWith("images/default_person")) {
                url = "/Users/weiqing/Documents/cs2103/debt-tracker/docs/images/default_person.png";
            }


            URL newtry = new File(url).toURI().toURL();
            imageView.setImage(new Image(String.valueOf(newtry), 208, 208, true, false));

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            URL newtry;
            try {
                newtry = new File(DEFAULT_IMAGE).toURI().toURL();
                imageView.setImage(new Image(String.valueOf(newtry), 208, 208, true, true));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TransactionCard)) {
            return false;
        }

        // state check
        TransactionCard card = (TransactionCard) other;
        return id.getText().equals(card.id.getText())
                && transaction.equals(card.transaction);
    }
}
