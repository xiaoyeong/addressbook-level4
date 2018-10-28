package seedu.address.commons.core;

/**
 * Represents error messages displayed to the user
 */
public class Messages {
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_INTEREST_RATE =
            "The interest value must be a real number rounded to two places";
    public static final String MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX =
            "The transaction index provided is invalid";

    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The transaction index provided is invalid";
    public static final String MESSAGE_TRANSACTIONS_LISTED_OVERVIEW = "%1$d transactions listed!";
    public static final String MESSAGE_INVALID_DATE =
            "The transaction deadline must be a valid date in the future in the DD/MM/YYYY format";
}
