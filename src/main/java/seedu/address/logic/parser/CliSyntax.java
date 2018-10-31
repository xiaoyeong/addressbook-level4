package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {
    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_TRANSACTION_AMOUNT = new Prefix("ta/");
    public static final Prefix PREFIX_TRANSACTION_TYPE = new Prefix("tt/");
    public static final Prefix PREFIX_TRANSACTION_DEADLINE = new Prefix("td/");
    public static final Prefix PREFIX_PHOTO_PATH = new Prefix("up/");
    public static final Prefix PREFIX_OR = new Prefix("or/");
    public static final Prefix PREFIX_AND = new Prefix("and/");
    public static final Prefix PREFIX_TRANSACTION_AMOUNT_MAX = new Prefix("tamax/");
    public static final Prefix PREFIX_TRANSACTION_AMOUNT_MIN = new Prefix("tamin/");
    public static final Prefix PREFIX_TRANSACTION_DEADLINE_EARLIEST = new Prefix("tdmin/");
    public static final Prefix PREFIX_TRANSACTION_DEADLINE_LATEST = new Prefix("tdmax/");

}
