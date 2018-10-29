package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

/**
 * Sorts transactions in the financial database based on a parameter.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SUCCESS = "%d transactions sorted by %s attribute!";
    public static final Object DEFAULT_SORT_PARAMETER = "person";
    private final String sortParameter;

    public SortCommand(String arguments) {
        requireNonNull(arguments);
        sortParameter = arguments;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        ObservableList<Transaction> originalTransactionList = model.getFilteredTransactionList();
        int size = originalTransactionList.size();
        Comparator<Transaction> transactionComparator;
        String trimmedSortParameterLowerCase = sortParameter.toLowerCase().trim();
        String commandResult;
        switch(trimmedSortParameterLowerCase) {
        case "amount":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Amount firstAmount = firstTransaction.getAmount();
                Amount secondAmount = secondTransaction.getAmount();
                return firstAmount.compareTo(secondAmount);
            };
            commandResult = String.format(MESSAGE_SUCCESS, size, trimmedSortParameterLowerCase);
            break;
        case "deadline":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Deadline firstDeadline = firstTransaction.getDeadline();
                Deadline secondDeadline = secondTransaction.getDeadline();
                return firstDeadline.compareTo(secondDeadline);
            };
            commandResult = String.format(MESSAGE_SUCCESS, size, trimmedSortParameterLowerCase);
            break;
        case "type":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Type firstType = firstTransaction.getType();
                Type secondType = secondTransaction.getType();
                return firstType.compareTo(secondType);
            };
            commandResult = String.format(MESSAGE_SUCCESS, size, trimmedSortParameterLowerCase);
            break;
        default:
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Person firstPerson = firstTransaction.getPerson();
                Person secondPerson = secondTransaction.getPerson();
                return firstPerson.compareTo(secondPerson);
            };
            commandResult = String.format(MESSAGE_SUCCESS, size, DEFAULT_SORT_PARAMETER);
        }
        List<Transaction> sortedTransactionList = new ArrayList<>(originalTransactionList);
        sortedTransactionList.sort(transactionComparator);
        model.resetData(model.getFinancialDatabase());
        for (Transaction newTransaction : sortedTransactionList) {
            model.addTransaction(newTransaction);
        }
        return new CommandResult(commandResult);
    }
}
