package seedu.address.model.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import seedu.address.testutil.TransactionBuilder;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.TypicalTransactions;

public class TransactionTest {
    @Test
    public void equals() {
        Transaction firstTransaction = TypicalTransactions.AMY_TRANSACTION;
        assertEquals(firstTransaction, firstTransaction);

        Transaction secondTransaction = TypicalTransactions.BENSON_TRANSACTION;
        assertNotEquals(firstTransaction, secondTransaction);

        Transaction copyFirstTransaction = Transaction.copy(TypicalTransactions.AMY_TRANSACTION);
        assertEquals(firstTransaction, copyFirstTransaction);

        Transaction firstTransactionChangedAmount = new TransactionBuilder()
                .withPerson(TypicalPersons.AMY)
                .withAmount("SGD 245.60")
                .withType("Loan")
                .withDeadline("17/11/2018").build();

        assertNotEquals(firstTransactionChangedAmount, firstTransaction);

        Transaction firstTransactionChangedType = new TransactionBuilder()
                .withPerson(TypicalPersons.AMY)
                .withAmount("SGD 145.60")
                .withType("Debt")
                .withDeadline("17/11/2018").build();

        assertNotEquals(firstTransactionChangedType, firstTransaction);

        Transaction firstTransactionChangedPerson = new TransactionBuilder()
                .withPerson(TypicalPersons.BOB)
                .withAmount("SGD 145.60")
                .withType("Debt")
                .withDeadline("17/11/2018").build();

        assertNotEquals(firstTransactionChangedPerson, firstTransaction);

        Transaction firstTransactionChangedDeadline = new TransactionBuilder()
                .withPerson(TypicalPersons.AMY)
                .withAmount("SGD 145.60")
                .withType("Debt")
                .withDeadline("17/11/2019").build();

        assertNotEquals(firstTransactionChangedPerson, firstTransaction);

        assertNotEquals(firstTransaction, new TransactionBuilder());

    }
}
