package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

/**
 * Uploads a photo for a person involved in a transaction with the user.
 */
public class UploadPhotoCommand extends Command {
    public static final String COMMAND_WORD = "uploadphoto";
    public static final String COMMAND_ALIAS = "uploadp";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": upload image to that transaction contact";

    private String filePath;
    private Index photoIndex;

    public UploadPhotoCommand(Index index, String path) {
        //make sure input not null
        requireNonNull(index);
        requireNonNull(path);

        photoIndex = index;
        filePath = path;
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Transaction> lastTransactionList = model.getFilteredTransactionList();
        int lastPersonListIndex = lastTransactionList.size();
        int thatPersonIndex = photoIndex.getZeroBased();
        if (thatPersonIndex >= lastPersonListIndex) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return null;
    }
}
