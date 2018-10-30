package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AnalyticsCommand;
import seedu.address.logic.commands.CalendarCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConvertCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.InterestCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NextTransactionCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.ToDoListAddCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UploadPhotoCommand;
import seedu.address.logic.commands.WildcardSearchCommand;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class FinancialDatabaseParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case AnalyticsCommand.COMMAND_WORD:
        case AnalyticsCommand.COMMAND_ALIAS:
            return new AnalyticsCommandParser().parse(arguments);

        case CalendarCommand.COMMAND_WORD:
        case CalendarCommand.COMMAND_ALIAS:
            return new CalendarCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case ConvertCommand.COMMAND_WORD:
        case ConvertCommand.COMMAND_ALIAS:
            return new ConvertCommand();

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            return new DeleteCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_ALIAS:
            return new ExitCommand();

        case FilterCommand.COMMAND_WORD:
        case FilterCommand.COMMAND_ALIAS:
            return new FilterCommandParser().parse(arguments);

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_ALIAS:
            return new HelpCommand();

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case InterestCommand.COMMAND_WORD:
        case InterestCommand.COMMAND_ALIAS:
            return new InterestCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case NextTransactionCommand.COMMAND_WORD:
        case NextTransactionCommand.COMMAND_ALIAS:
            return new NextTransactionCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        case SortCommand.COMMAND_WORD:
            return new SortCommand(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case ToDoListAddCommand.COMMAND_WORD:
        case ToDoListAddCommand.COMMAND_ALIAS:
            return new ToDoListAddCommandParser().parse(arguments);

        case WildcardSearchCommand.COMMAND_WORD:
        case WildcardSearchCommand.COMMAND_ALIAS:
            return new WildcardSearchCommandParser().parse(arguments);

        case UploadPhotoCommand.COMMAND_WORD:
        case UploadPhotoCommand.COMMAND_ALIAS:
            return new UpdatePhotoCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
