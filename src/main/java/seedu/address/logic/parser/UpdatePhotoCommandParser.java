package seedu.address.logic.parser;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UploadPhotoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO_PATH;


//@@author weiqing-nic

/**
 * Parses input arguments and creates a new UpdatePhotoCommand object
 */
public class UpdatePhotoCommandParser implements Parser<UploadPhotoCommand> {

    public UploadPhotoCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PHOTO_PATH);
        System.out.println("argMultimap");
        String tt = requireNonNull(argMultimap.getValue(PREFIX_PHOTO_PATH).get());
        System.out.println(tt);

        if (!arePrefixesPresent(argMultimap, PREFIX_PHOTO_PATH)  || argMultimap.getPreamble().isEmpty()) {
            System.out.println("inside");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadPhotoCommand.MESSAGE_USAGE));
        }

        //get the currentTransaction Index and the currentTransactionPhoto
        Index getcurrentTransactionIndex = requireNonNull(Index.fromOneBased( Integer.parseInt( argMultimap.getPreamble()) ));
        String getPhotoPath = requireNonNull(argMultimap.getValue(PREFIX_PHOTO_PATH).get());

        System.out.println("uploading");
        System.out.println(getPhotoPath);
        System.out.println(getcurrentTransactionIndex);
        return new UploadPhotoCommand(getcurrentTransactionIndex, getPhotoPath );

    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }



}
