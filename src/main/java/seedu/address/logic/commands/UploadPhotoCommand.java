package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

/**
 * Uploads a photo for a person involved in a transaction with the user.
 */
public class UploadPhotoCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Photo Changed: Update User %1$s";
    public static final int DEFAULT_INDEX = 1;
    public static final String COMMAND_WORD = "updatephoto";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": upload image to that transaction contact";

    private final Logger logger = LogsCenter.getLogger(getClass());
    private String filePath;
    private Index photoIndex;

    public UploadPhotoCommand(Index index, String path) {
        logger.info("UploadPhotoclass");
        logger.info(path);
        //make sure input not null
        requireNonNull(index);
        requireNonNull(path);

        photoIndex = index;
        filePath = path;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Transaction> latestTransactionList = model.getFilteredTransactionList();
        int lastPersonListIndex = latestTransactionList.size();
        int zeroBasedTransationIndex = photoIndex.getZeroBased();
        int oneBasedTransactionIndex = photoIndex.getOneBased();
        if (zeroBasedTransationIndex >= lastPersonListIndex) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Transaction currentTransaction = latestTransactionList.get(zeroBasedTransationIndex);
        Transaction editTransaction = Transaction.copy(currentTransaction);

        logger.info("before fail");
        logger.info(filePath);

        try {
            editTransaction.setPhoto(filePath);
        } catch (IllegalValueException e) {
            logger.info("cannot add");
        }

        model.updateTransaction(currentTransaction, editTransaction);
        model.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        model.commitFinancialDatabase();


        return new CommandResult(String.format(MESSAGE_SUCCESS, oneBasedTransactionIndex));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UploadPhotoCommand // instanceof handles nulls
                && this.photoIndex.equals(((UploadPhotoCommand) other).photoIndex)
                && this.filePath.equals(((UploadPhotoCommand) other).filePath));
    }
}
