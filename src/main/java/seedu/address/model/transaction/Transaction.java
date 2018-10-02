package seedu.address.model.transaction;

import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {

    private final Type type;
    private final Amount amount;
    private final Person person;

    /**
     * Represents a transaction with non null fields {@code Type} and {@code amount}
     * @param type indicates whether a transaction is a loan/debt
     * @param amount stores the amount lent/owed
     */
    public Transaction(Type type, Amount amount, Person person) {
        this.type = type;
        this.amount = amount;
        this.person = person;
    }

    public Type getType() {
        return type;
    }

    public Amount getAmount() {
        return amount;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, person);
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
