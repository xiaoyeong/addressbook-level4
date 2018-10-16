package seedu.address.logic.commands;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;


public class UploadPhotoCommand extends Command{

    public static final String COMMAND_WORD = "uploadphoto";
    public static final String COMMAND_ALIAS = "uploadp";

    private String path;
    private String filePath;
    private Index photoIndex;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": upload image to that person contact";

    public UploadPhotoCommand(Index index, String imagePath){

        //make sure input not null
        requireNonNull(index);
        requireNonNull(imagePath);

        photoIndex = index;
        path = imagePath;
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        List<Person>  lastPersonList  = model.getFilteredPersonList();

        int lastPersonListIndex = lastPersonList.size();

        int thatPersonIndex = photoIndex.getZeroBased();

        int editedPerson;

        if (thatPersonIndex >=  lastPersonListIndex ) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

//        = lastPersonList.get(thatPersonIndex);


//        model.updateFilteredPersonList();











        return null;
    }

}
