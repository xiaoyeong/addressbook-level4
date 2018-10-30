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
    public static final String MESSAGE_INVALID_PREFIX_VALUE = "Prefix %1$s should not have any values";
    public static final String MESSAGE_KEYWORDS_NONEMPTY = "None of the specified keywords should be empty";
    public static final String MESSAGE_INVALID_PREFIX_COMBINATION = "Prefix %1$s and %2$s cannot be used together";
}
