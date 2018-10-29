package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.requireNonNull;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Amount;
import seedu.address.model.transaction.Deadline;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.Type;

public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SUCCESS = "%d transactions sorted by %s attribute!";
    private static final String MESSAGE_INCORRECT_SORT_PARAMETER = "The sort parameter provided is invalid";
    public final String sortParameter;

    public SortCommand(String arguments) {
        requireNonNull(arguments);
        sortParameter = arguments;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        ObservableList<Transaction> originalTransactionList = model.getFilteredTransactionList();
        Comparator<Transaction> transactionComparator;
        String trimmedSortParameterLowerCase = sortParameter.toLowerCase().trim();
        switch(trimmedSortParameterLowerCase) {
        case "amount":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Amount firstAmount = firstTransaction.getAmount();
                Amount secondAmount = secondTransaction.getAmount();
                return firstAmount.compareTo(secondAmount);
            };
            break;
        case "deadline":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Deadline firstDeadline = firstTransaction.getDeadline();
                Deadline secondDeadline = secondTransaction.getDeadline();
                return firstDeadline.compareTo(secondDeadline);
            };
            break;
        case "person":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Person firstPerson = firstTransaction.getPerson();
                Person secondPerson = secondTransaction.getPerson();
                return firstPerson.compareTo(secondPerson);
            };
            break;
        case "type":
            transactionComparator = (firstTransaction, secondTransaction) -> {
                Type firstType = firstTransaction.getType();
                Type secondType = secondTransaction.getType();
                return firstType.compareTo(secondType);
            };
            break;
        default:
            throw new CommandException(MESSAGE_INCORRECT_SORT_PARAMETER);
        }
        List<Transaction> sortedTransactionList = new ArrayList<>(originalTransactionList);
        sortedTransactionList.sort(transactionComparator);
        int size = originalTransactionList.size();
        model.resetData(model.getFinancialDatabase());
        for (Transaction newTransaction : sortedTransactionList) {
            model.addTransaction(newTransaction);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, size, trimmedSortParameterLowerCase));
    }
}
