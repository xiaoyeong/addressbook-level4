package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.EditCommand.EditTransactionDescriptor;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AnalyticsCommand;
import seedu.address.logic.commands.CalendarCommand;
import seedu.address.logic.commands.ClearCommand;
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
import seedu.address.logic.commands.PaidCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UploadPhotoCommand;
import seedu.address.logic.commands.WildcardSearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsLettersPredicate;
import seedu.address.model.person.Photo;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.AmountBoundsPredicate;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.DeadlineBoundsPredicate;
import seedu.address.model.transaction.FieldContainsKeywordsPredicate;
import seedu.address.model.transaction.MultiFieldPredicate;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.EditTransactionDescriptorBuilder;
import seedu.address.testutil.TransactionBuilder;
import seedu.address.testutil.TransactionUtil;
import seedu.address.testutil.TypicalPersons;


public class FinancialDatabaseParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final FinancialDatabaseParser parser = new FinancialDatabaseParser();

    @Test
    public void parseCommand_add() throws Exception {
        Transaction transaction = new TransactionBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(TransactionUtil.getAddCommand(transaction));
        assertEquals(new AddCommand(transaction), command);
    }

    @Test
    public void parseCommand_analytics() throws Exception {
        AnalyticsCommand command = (AnalyticsCommand)
                parser.parseCommand(AnalyticsCommand.COMMAND_WORD + " " + Deadline.CURRENT_DATE);
        Deadline todayDeadline = new Deadline(Deadline.CURRENT_DATE);
        assertEquals(new AnalyticsCommand(todayDeadline), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_calendar() throws Exception {
        CalendarCommand firstCommand = (CalendarCommand) parser.parseCommand(
                CalendarCommand.COMMAND_WORD + " " + CalendarCommand.SHOW_ACTION);
        CalendarCommand secondCommand = (CalendarCommand) parser.parseCommand(
                CalendarCommand.COMMAND_WORD + " " + CalendarCommand.LOGIN_ACTION);
        CalendarCommand thirdCommand = (CalendarCommand) parser.parseCommand(
                CalendarCommand.COMMAND_WORD + " " + CalendarCommand.LOGOUT_ACTION);
        CalendarCommand fourthCommand = (CalendarCommand) parser.parseCommand(
                CalendarCommand.COMMAND_WORD + " " + CalendarCommand.SYNC_ACTION);

        assertEquals(new CalendarCommand(CalendarCommand.SHOW_ACTION), firstCommand);
        assertEquals(new CalendarCommand(CalendarCommand.LOGIN_ACTION), secondCommand);
        assertEquals(new CalendarCommand(CalendarCommand.LOGOUT_ACTION), thirdCommand);
        assertEquals(new CalendarCommand(CalendarCommand.SYNC_ACTION), fourthCommand);
    }

    @Test
    public void parseCommand_convert() throws Exception {
        assertTrue(parser.parseCommand(ConvertCommand.COMMAND_WORD) instanceof ConvertCommand);
        assertTrue(parser.parseCommand(ConvertCommand.COMMAND_WORD + " 3") instanceof ConvertCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_TRANSACTION), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Transaction transaction = new TransactionBuilder().build();
        EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(transaction).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_TRANSACTION.getOneBased() + " "
                + TransactionUtil.getEditTransactionDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_TRANSACTION, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        List<Predicate<Transaction>> predicates = new ArrayList<>();

        List<String> nameList = new ArrayList<>();
        String aliceName = TypicalPersons.ALICE.getName().toString();
        String danielName = TypicalPersons.DANIEL.getName().toString();
        nameList.add(TypicalPersons.ALICE.getName().toString());
        nameList.add(TypicalPersons.DANIEL.getName().toString());

        predicates.add(new FieldContainsKeywordsPredicate(FieldType.Name, nameList));
        predicates.add(new DeadlineBoundsPredicate(new Deadline(Deadline.CURRENT_DATE),
                DeadlineBoundsPredicate.BoundType.EARLIEST));
        predicates.add(new AmountBoundsPredicate(Amount.DEFAULT_AMOUNT, AmountBoundsPredicate.BoundType.MIN));
        FilterCommand testCommand = new FilterCommand(predicates, MultiFieldPredicate.OperatorType.AND);

        FilterCommand parsedCommand = (FilterCommand) parser.parseCommand(FilterCommand.COMMAND_WORD + " "
                        + CliSyntax.PREFIX_NAME + " " + aliceName + ";" + danielName + " "
                        + CliSyntax.PREFIX_TRANSACTION_DEADLINE_EARLIEST + " " + Deadline.CURRENT_DATE
                        + " " + CliSyntax.PREFIX_TRANSACTION_AMOUNT_MIN + " "
                        + String.format("%.2f", Amount.DEFAULT_AMOUNT.getValue()));

        assertEquals(parsedCommand, testCommand);
    }


    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_interest() throws Exception {
        String interestScheme = "simple";
        String rate = "3.45%";
        InterestCommand command = (InterestCommand) parser.parseCommand(
                InterestCommand.COMMAND_WORD + " " + interestScheme + " " + rate);
        assertEquals(new InterestCommand(interestScheme, rate), command);
    }

    @Test
    public void parseCommand_nextTransaction() throws Exception {
        assertTrue(parser.parseCommand(NextTransactionCommand.COMMAND_WORD) instanceof NextTransactionCommand);
        assertTrue(parser.parseCommand(NextTransactionCommand.COMMAND_WORD + " 3")
                instanceof NextTransactionCommand);
    }

    @Test
    public void parseCommand_wildCardSearch() throws Exception {
        String test = "Al";
        List<String> letters = List.of(test);
        NameContainsLettersPredicate predicate = new NameContainsLettersPredicate(letters);
        WildcardSearchCommand command = (WildcardSearchCommand) parser.parseCommand(
                WildcardSearchCommand.COMMAND_WORD + " " + test);
        assertEquals(new WildcardSearchCommand(predicate), command);
    }

    @Test
    public void parseCommand_uploadPhoto() throws Exception {
        UploadPhotoCommand command = (UploadPhotoCommand) parser.parseCommand(UploadPhotoCommand.COMMAND_WORD
                + " " + UploadPhotoCommand.DEFAULT_INDEX + " " + CliSyntax.PREFIX_PHOTO_PATH + " "
                + Photo.DEFAULT_PHOTO_PATH);
        assertEquals(new UploadPhotoCommand(Index.fromOneBased(UploadPhotoCommand.DEFAULT_INDEX),
                        Photo.DEFAULT_PHOTO_PATH), command);
    }

    @Test
    public void parseCommand_paid() throws Exception {
        PaidCommand command = (PaidCommand) parser.parseCommand(PaidCommand.COMMAND_WORD + " "
                + PaidCommand.DEFAULT_INDEX);
        assertEquals(new PaidCommand(Index.fromOneBased(PaidCommand.DEFAULT_INDEX)), command);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD) instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " " + SortCommand.AMOUNT_SORT_PARAMETER)
                instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " " + SortCommand.TYPE_SORT_PARAMETER)
                instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " " + SortCommand.DEADLINE_SORT_PARAMETER)
                instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD) instanceof SortCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_TRANSACTION.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_TRANSACTION), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
