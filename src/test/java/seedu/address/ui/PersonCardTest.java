package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysTransaction;

import guitests.guihandles.TransactionCardHandle;
import org.junit.Test;

import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.TransactionBuilder;

public class PersonCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Transaction personWithNoTags = new TransactionBuilder().withTags(new String[0]).build();
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
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different transaction, same index -> returns false
        Transaction differentPerson = new TransactionBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new TransactionCard(differentPerson, 0)));

        // same transaction, different index -> returns false
        assertFalse(personCard.equals(new TransactionCard(person, 1)));
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
