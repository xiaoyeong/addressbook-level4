package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TRANSACTION;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AnalyticsCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ConvertCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.EditTransactionDescriptorBuilder;
import seedu.address.testutil.TransactionBuilder;
import seedu.address.testutil.TransactionUtil;


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
        assertEquals(new AnalyticsCommand(Deadline.CURRENT_DATE), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
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
        EditCommand.EditTransactionDescriptor descriptor = new EditTransactionDescriptorBuilder(transaction).build();
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
        assertTrue(parser.parseCommand(ConvertCommand.COMMAND_WORD) instanceof ConvertCommand);
        assertTrue(parser.parseCommand(ConvertCommand.COMMAND_WORD + " 3") instanceof ConvertCommand);
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
