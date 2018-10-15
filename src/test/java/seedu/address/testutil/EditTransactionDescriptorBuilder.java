package seedu.address.testutil;

import seedu.address.logic.commands.EditCommand.EditTransactionDescriptor;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.PersonId;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 * A utility class to help with building EditTransactionDescriptor objects.
 */
public class EditTransactionDescriptorBuilder{

    private EditTransactionDescriptor descriptor;

    public EditTransactionDescriptorBuilder() {
        descriptor = new EditTransactionDescriptor();
    }

    public EditTransactionDescriptorBuilder(EditTransactionDescriptor descriptor) {
        this.descriptor = new EditTransactionDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTransactionDescriptor} with fields containing {@code person}'s details
     */
    public EditTransactionDescriptorBuilder(Transaction transaction) {
        descriptor = new EditTransactionDescriptor();
        descriptor.setAmount(transaction.getAmount());
        descriptor.setType(transaction.getType());
        descriptor.setPersonId(transaction.getPersonId());
    }

    /**
     * Sets the {@code Amount} of the {@code EditTransactionDescriptor} that we are building.
     */
    public EditTransactionDescriptorBuilder withAmount(String amount) {
        descriptor.setAmount(new Amount(amount));
        return this;
    }

    /**
     * Sets the {@code Type} of the {@code EditTransactionDescriptor} that we are building.
     */
    public EditTransactionDescriptorBuilder withType(String type) {
        descriptor.setType(new Type(type));
        return this;
    }

    /**
     * Sets the {@code Person} of the {@code EditTransactionDescriptor} that we are building.
     */
    public EditTransactionDescriptorBuilder withPersonId(String personid) {
        descriptor.setPersonId(new PersonId(personid));
        return this;
    }


    public EditTransactionDescriptor build() {
        return descriptor;
    }
}
