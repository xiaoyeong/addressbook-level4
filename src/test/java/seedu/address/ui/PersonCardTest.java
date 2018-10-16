package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysTransaction;

import guitests.guihandles.TransactionCardHandle;
import org.junit.Test;

import seedu.address.model.person.Transaction;

public class PersonCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Transaction personWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(personWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, personWithNoTags, 1);

        // with tags
        Transaction personWithTags = new PersonBuilder().build();
        personCard = new PersonCard(personWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, personWithTags, 2);
    }

    @Test
    public void equals() {
        Transaction person = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(person, 0);

        // same transaction, same index -> returns true
        PersonCard copy = new PersonCard(person, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different transaction, same index -> returns false
        Transaction differentPerson = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentPerson, 0)));

        // same transaction, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(person, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Transaction expectedPerson, int expectedId) {
        guiRobot.pauseForHuman();

        TransactionCardHandle transactionCardHandle = new TransactionCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", transactionCardHandle.getId());

        // verify transaction details are displayed correctly
        assertCardDisplaysTransaction(expectedPerson, transactionCardHandle);
    }
}
