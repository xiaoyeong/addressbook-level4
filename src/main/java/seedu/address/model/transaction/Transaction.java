package seedu.address.model.transaction;

import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * {@code Transaction} class encapsulates a transaction added to the financial database
 */
public class Transaction {

    private final Type type;
    private final Amount amount;
    private final PersonId personid;

    /**
     * Represents a transaction with non null fields {@code Type} and {@code amount}
     * @param type indicates whether a transaction is a loan/debt
     * @param amount stores the amount lent/owed
     * @param personid the person who is carrying out transaction with the user
     */
    public Transaction(Type type, Amount amount, PersonId personid) {
        this.type = type;
        this.amount = amount;
        this.personid = personid;
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
