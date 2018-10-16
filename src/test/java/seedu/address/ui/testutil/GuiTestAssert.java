package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.TransactionCardHandle;
import guitests.guihandles.TransactionListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(TransactionCardHandle expectedCard, TransactionCardHandle actualCard) {
        assertEquals(expectedCard.getType(), actualCard.getType());
        assertEquals(expectedCard.getAmount(), actualCard.getAmount());
        assertEquals(expectedCard.getDeadline(), actualCard.getDeadline());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getEmail(), actualCard.getEmail());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedTransaction}.
     */
    public static void assertCardDisplaysTransaction(Transaction expectedTransaction, TransactionCardHandle actualCard) {
        Person person = expectedTransaction.getPerson();
        assertEquals(expectedTransaction.getType().value, actualCard.getType());
        assertEquals(expectedTransaction.getAmount().value, actualCard.getAmount());
        assertEquals(expectedTransaction.getDeadline().value, actualCard.getDeadline());
        assertEquals(person.getName().fullName, actualCard.getName());
        assertEquals(person.getPhone().value, actualCard.getPhone());
        assertEquals(person.getEmail().value, actualCard.getEmail());
        assertEquals(person.getAddress().value, actualCard.getAddress());
        assertEquals(person.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that the list in {@code transactionListPanelHandle} displays the details of {@code persons} correctly and
     * in the correct order.
     */
    public static void assertListMatching(TransactionListPanelHandle transactionListPanelHandle, Transaction... persons) {
        for (int i = 0; i < persons.length; i++) {
            transactionListPanelHandle.navigateToCard(i);
            assertCardDisplaysTransaction(persons[i], transactionListPanelHandle.getPersonCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code transactionListPanelHandle} displays the details of {@code persons} correctly and
     * in the correct order.
     */
    public static void assertListMatching(TransactionListPanelHandle transactionListPanelHandle, List<Transaction> persons) {
        assertListMatching(transactionListPanelHandle, persons.toArray(new Transaction[0]));
    }

    /**
     * Asserts the size of the list in {@code transactionListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(TransactionListPanelHandle transactionListPanelHandle, int size) {
        int numberOfPeople = transactionListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
