package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;


/**
 * Adds a person to the address book.
 */
public class AddTransactionCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a transaction to the debt tracker. "
            + "Parameters: "
            + PREFIX_PERSONID + "PERSON ID "
            + PREFIX_TRANSACTION_AMOUNT + "AMOUNT "
            + PREFIX_TRANSACTION_TYPE + "TYPE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_PERSONID + "1 "
            + PREFIX_TRANSACTION_AMOUNT + "98765432 "
            + PREFIX_TRANSACTION_TYPE + "debt ";

    public static final String MESSAGE_SUCCESS = "New transaction added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Transaction toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddTransactionCommand(Transaction transaction) {
        requireNonNull(transaction);
        toAdd = transaction;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasTransaction(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addTransaction(toAdd);
        model.commitFinancialDatabase();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddTransactionCommand // instanceof handles nulls
                && toAdd.equals(((AddTransactionCommand) other).toAdd));
    }
}
