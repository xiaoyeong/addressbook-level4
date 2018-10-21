package seedu.address.logic.parser;

import seedu.address.commons.core.Messages;
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

        if (!arePrefixesPresent(argMultimap, PREFIX_PHOTO_PATH)  || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_INVALID_COMMAND_FORMAT));
        }

        //get the currentTransaction Index and the currentTransactionPhoto

        String getPhoto = requireNonNull(argMultimap.getPreamble().trim());
        String getcurrentTransactionIndex = requireNonNull(argMultimap.getPreamble());





        return null;
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }



}
