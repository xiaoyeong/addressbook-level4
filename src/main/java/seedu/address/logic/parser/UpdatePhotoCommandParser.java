package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO_PATH;

import java.util.logging.Logger;
import java.util.stream.Stream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UploadPhotoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UpdatePhotoCommand object
 */
public class UpdatePhotoCommandParser implements Parser<UploadPhotoCommand> {
    private final Logger logger = LogsCenter.getLogger(getClass());

    /**
     * Parses the given {@code String} of arguments in the context of the UpdatePhotoCommand
     * and returns an UpdatePhotoCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UploadPhotoCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PHOTO_PATH);

        if (!arePrefixesPresent(argMultimap, PREFIX_PHOTO_PATH) || argMultimap.getPreamble().isEmpty()) {
            logger.info("inside");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadPhotoCommand.MESSAGE_USAGE));
        }

        //get the currentTransaction Index and the currentTransactionPhoto
        Index getcurrentTransactionIndex = requireNonNull(
                Index.fromOneBased(Integer.parseInt(argMultimap.getPreamble())));
        String getPhotoPath = requireNonNull(argMultimap.getValue(PREFIX_PHOTO_PATH).get());

        logger.info("uploading");
        logger.info(getPhotoPath);
        logger.info("" + getcurrentTransactionIndex);
        return new UploadPhotoCommand(getcurrentTransactionIndex, getPhotoPath);

    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }



}
