package seedu.address.logic.commands;
<<<<<<< HEAD
=======

import static java.util.Objects.requireNonNull;

import java.util.List;

>>>>>>> 62ae91b7be5ee063260e9a6d45587902dc3600ec
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Transaction;

<<<<<<< HEAD
import static java.util.Objects.requireNonNull;


public class UploadPhotoCommand extends Command{
=======
/**
 * Uploads the photo of the transaction in a transaction for record keeping purposes.
 */
public class UploadPhotoCommand extends Command {
>>>>>>> 62ae91b7be5ee063260e9a6d45587902dc3600ec

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

<<<<<<< HEAD
        photoIndex = index;
        path = imagePath;
=======
        imageindex = index;
        filePath = path;
>>>>>>> 62ae91b7be5ee063260e9a6d45587902dc3600ec
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        List<Transaction> lastTransactionList = model.getFilteredTransactionList();

        int lastPersonListIndex = lastTransactionList.size();

        int thatPersonIndex = photoIndex.getZeroBased();

        int editedPerson;

<<<<<<< HEAD
        if (thatPersonIndex >=  lastPersonListIndex ) {
=======
        if (thatPerson >= lastPersonListIndex) {
>>>>>>> 62ae91b7be5ee063260e9a6d45587902dc3600ec
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

//        = lastPersonList.get(thatPersonIndex);


//        model.updateFilteredPersonList();











        return null;
    }

}
