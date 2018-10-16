package seedu.address.model.transaction;

import seedu.address.model.person.Person;

import java.util.Objects;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {

    private final Type type;
    private final Amount amount;
    private final Person person;
    private final Deadline deadline;

    /**
     * Represents a transaction with non null fields {@code type}, {@code amount}, {@code deadline}
     * and {@code transaction}
     * @param type type of transaction, either a loan or a debt
     * @param amount the amount lent/owed by creditor/debtor respectively
     * @param deadline the date on which the payment is to be made
     * @param person the transactor loaning/borrowing the {@code amount}
     */
    public Transaction(Type type, Amount amount, Deadline deadline, Person person) {
        this.type = type;
        this.amount = amount;
        this.person = person;
        this.deadline = deadline;
    }


    public Type getType() {
        return type;
    }

    public Amount getAmount() {
        return amount;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, deadline, person);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction transaction = (Transaction) other;
        return other == this || (type.equals(transaction.type)
                && amount.equals(transaction.amount)
                && person.equals(transaction.person));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Person: ")
               .append(person.toString())
               .append("\n Transaction Details: \n")
               .append("Type: ")
               .append(type)
               .append("\n Amount: ")
               .append(amount);
        return builder.toString();
    }
}
