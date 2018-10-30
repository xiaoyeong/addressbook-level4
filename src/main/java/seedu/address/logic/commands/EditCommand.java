package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRANSACTION_TYPE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 * Edits the details of an existing transaction in the financial database.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "ed";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the transaction identified "
            + "by the index number used in the displayed transaction list. "
            + "Existing values will be overwritten by the input values.\n"
            + "[" + PREFIX_TRANSACTION_TYPE + "NAME] "
            + "[" + PREFIX_TRANSACTION_AMOUNT + "AMOUNT] "
            + "[" + PREFIX_TRANSACTION_DEADLINE + "DEADLINE] "
            + "Parameters: INDEX (must be a positive integer)"
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_TRANSACTION_SUCCESS = "Edited transaction: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TRANSACTION = "This transaction already exists in the address book.";

    private final Index index;
    private final EditTransactionDescriptor editTransactionDescriptor;

    /**
     * @param index of the transaction in the filtered transaction list to edit
     * @param editTransactionDescriptor details to edit the transaction with
     */
    public EditCommand(Index index, EditTransactionDescriptor editTransactionDescriptor) {
        requireNonNull(index);
        requireNonNull(editTransactionDescriptor);

        this.index = index;
        this.editTransactionDescriptor = new EditTransactionDescriptor(editTransactionDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Transaction> lastShownList = model.getFilteredTransactionList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
        }

        Transaction transactionToEdit = lastShownList.get(index.getZeroBased());
        Transaction editedTransaction = createEditedTransaction(transactionToEdit,
                editTransactionDescriptor);

        if (!transactionToEdit.equals(editedTransaction) && model.hasTransaction(editedTransaction)) {
            throw new CommandException(MESSAGE_DUPLICATE_TRANSACTION);
        }

        model.updateTransaction(transactionToEdit, editedTransaction);
        model.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        model.commitFinancialDatabase();
        return new CommandResult(String.format(MESSAGE_EDIT_TRANSACTION_SUCCESS, editedTransaction));
    }

    /**
     * Creates and returns a {@code transaction} with the details of {@code transactionToEdit}
     * edited with {@code editTransactionDescriptor}.
     */
    private static Transaction createEditedTransaction(Transaction transactionToEdit,
                                                       EditTransactionDescriptor editTransactionDescriptor) {
        requireNonNull(transactionToEdit);

        Type updatedType = editTransactionDescriptor.getType().orElse(transactionToEdit.getType());
        Amount updatedAmount = editTransactionDescriptor.getAmount().orElse(transactionToEdit.getAmount());
        Deadline updatedDeadline = editTransactionDescriptor.getDeadline().orElse(transactionToEdit.getDeadline());

        Name updatedName = editTransactionDescriptor.getName().orElse(transactionToEdit.getPerson().getName());
        Phone updatedPhone = editTransactionDescriptor.getPhone().orElse(transactionToEdit.getPerson().getPhone());
        Email updatedEmail = editTransactionDescriptor.getEmail().orElse(transactionToEdit.getPerson().getEmail());
        Address updatedAddress = editTransactionDescriptor.getAddress()
                .orElse(transactionToEdit.getPerson().getAddress());
        Set<Tag> updatedTags = editTransactionDescriptor.getTags().orElse(transactionToEdit.getPerson().getTags());
        Person updatedPerson = new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
        return new Transaction(updatedType, updatedAmount, updatedDeadline, updatedPerson);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editTransactionDescriptor.equals(e.editTransactionDescriptor);
    }

    /**
     * Stores the details to edit the transaction with. Each non-empty field value will replace the
     * corresponding field value of the transaction.
     */
    public static class EditTransactionDescriptor {
        private Amount amount;
        private Type type;
        private Deadline deadline;
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditTransactionDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTransactionDescriptor(EditTransactionDescriptor toCopy) {
            setAmount(toCopy.amount);
            setType(toCopy.type);
            setDeadline(toCopy.deadline);
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }


        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(amount, type, deadline, name, email, phone, address, tags);
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Optional<Amount> getAmount() {
            return Optional.ofNullable(amount);
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Optional<Type> getType() {
            return Optional.ofNullable(type);
        }

        public void setDeadline(Deadline deadline) {
            this.deadline = deadline;
        }

        public Optional<Deadline> getDeadline() {
            return Optional.ofNullable(deadline);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTransactionDescriptor)) {
                return false;
            }

            // state check
            EditTransactionDescriptor e = (EditTransactionDescriptor) other;

            return getAmount().equals(e.getAmount())
                    && getDeadline().equals(e.getDeadline())
                    && getType().equals(e.getType())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getTags().equals(e.getTags());
        }

    }
}

