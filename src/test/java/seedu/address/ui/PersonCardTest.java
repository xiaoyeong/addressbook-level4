package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysTransaction;

import org.junit.Test;

import guitests.guihandles.TransactionCardHandle;

import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.TransactionBuilder;

public class PersonCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Transaction personWithNoTags = new TransactionBuilder().withTags().build();
        TransactionCard personCard = new TransactionCard(personWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, personWithNoTags, 1);

        // with tags
        Transaction personWithTags = new TransactionBuilder().build();
        personCard = new TransactionCard(personWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, personWithTags, 2);
    }

    @Test
    public void equals() {
        Transaction person = new TransactionBuilder().build();
        TransactionCard personCard = new TransactionCard(person, 0);

        // same transaction, same index -> returns true
        TransactionCard copy = new TransactionCard(person, 0);
        assertEquals(personCard, copy);

        // same object -> returns true
        assertEquals(personCard, personCard);

        // null -> returns false
        assertNotEquals(null, personCard);

        // different types -> returns false
        assertNotEquals(0, personCard);

        // different transaction, same index -> returns false
        Transaction differentPerson = new TransactionBuilder().withName("differentName").build();
        assertNotEquals(personCard, new TransactionCard(differentPerson, 0));

        // same transaction, different index -> returns false
        assertNotEquals(personCard, new TransactionCard(person, 1));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(TransactionCard personCard, Transaction expectedPerson, int expectedId) {
        guiRobot.pauseForHuman();

        TransactionCardHandle transactionCardHandle = new TransactionCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", transactionCardHandle.getId());

        // verify transaction details are displayed correctly
        assertCardDisplaysTransaction(expectedPerson, transactionCardHandle);
    }
}
