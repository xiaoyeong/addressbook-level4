package seedu.address.model.transaction;

import java.util.Objects;
import seedu.address.model.transaction.PersonId;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {

    private final Type type;
    private final Amount amount;
    private final PersonId personid;
    private Deadline deadline;

    /**
     * Represents a transaction with non null fields {@code type}, {@code amount}, {@code personidid}
     * and {@code deadline}
     * @param type type of transaction, either a loan or a debt
     * @param amount the amount lent/owed by creditor/debtor respectively
     * @param personid the identifier for the personid who is carrying out transaction with the user
     * @param deadline the date on which the payment is to be made
     */
    public Transaction(Type type, Amount amount, PersonId personid, Deadline deadline) {
        this.type = type;
        this.amount = amount;
        this.personid = personid;
        this.deadline = deadline;
    }


    public Type getType() {
        return type;
    }

    public Amount getAmount() {
        return amount;
    }

    public PersonId getPersonId() {
        return personid;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, personid);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) other;
        return other == this || (type.equals(transaction.type)
                && amount.equals(transaction.amount)
                && personid.equals(transaction.personid));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Person: ")
               .append(personid.toString())
               .append("\n Transaction Details: \n")
               .append("Type: ")
               .append(type)
               .append("\n Amount: ")
               .append(amount);
        return builder.toString();
    }
}
