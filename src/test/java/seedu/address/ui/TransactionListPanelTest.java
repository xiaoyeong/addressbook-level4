package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;
import static seedu.address.testutil.TypicalTransactions.getTypicalTransactions;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysTransaction;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import guitests.guihandles.TransactionCardHandle;
import guitests.guihandles.TransactionListPanelHandle;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.transaction.Transaction;
import seedu.address.storage.XmlSerializableFinancialDatabase;

public class TransactionListPanelTest extends GuiUnitTest {
    private static final ObservableList<Transaction> TYPICAL_PERSONS =
            FXCollections.observableList(getTypicalTransactions());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_PERSON);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private TransactionListPanelHandle transactionListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_PERSONS);

        for (int i = 0; i < TYPICAL_PERSONS.size(); i++) {
            transactionListPanelHandle.navigateToCard(TYPICAL_PERSONS.get(i));
            Transaction expectedPerson = TYPICAL_PERSONS.get(i);
            TransactionCardHandle actualCard = transactionListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysTransaction(expectedPerson, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_PERSONS);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        TransactionCardHandle expectedPerson = transactionListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        TransactionCardHandle selectedPerson = transactionListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code TransactionListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Transaction> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of transaction cards exceeded time limit");
    }

    /**
     * Returns a list of persons containing {@code personCount} persons that is used to populate the
     * {@code TransactionListPanel}.
     */
    private ObservableList<Transaction> createBackingList(int personCount) throws Exception {
        Path xmlFile = createXmlFileWithPersons(personCount);
        XmlSerializableFinancialDatabase xmlAddressBook =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableFinancialDatabase.class);
        return FXCollections.observableArrayList(xmlAddressBook.toModelType().getTransactionList());
    }

    /**
     * Returns a .xml file containing {@code personCount} persons. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithPersons(int personCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<financialdatabase>\n");
        for (int i = 0; i < personCount; i++) {
            builder.append("<persons>\n");
            builder.append("<uniqueId>").append(Integer.toString(i + 1)).append("</uniqueId>");
            builder.append("<name>").append(i).append("a</name>\n");
            builder.append("<phone>000</phone>\n");
            builder.append("<email>a@aa</email>\n");
            builder.append("<address>a</address>\n");
            builder.append("</persons>\n");
        }
        builder.append("</financialdatabase>\n");

        Path manyPersonsFile = Paths.get(TEST_DATA_FOLDER + "manyPersons.xml");
        FileUtil.createFile(manyPersonsFile);
        FileUtil.writeToFile(manyPersonsFile, builder.toString());
        manyPersonsFile.toFile().deleteOnExit();
        return manyPersonsFile;
    }

    /**
     * Initializes {@code transactionListPanelHandle} with a {@code TransactionListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code TransactionListPanel}.
     */
    private void initUi(ObservableList<Transaction> backingList) {
        TransactionListPanel transactionListPanel = new TransactionListPanel(backingList);
        uiPartRule.setUiPart(transactionListPanel);

        transactionListPanelHandle = new TransactionListPanelHandle(getChildNode(transactionListPanel.getRoot(),
                TransactionListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}
