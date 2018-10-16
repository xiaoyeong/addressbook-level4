package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedTransaction.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalTransactions.BOB_TRANSACTION;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.testutil.Assert;

public class XmlAdaptedTransactionTest {
    private static final String INVALID_TYPE = "Borrow";
    private static final String INVALID_AMOUNT = "46.50";
    private static final String INVALID_DEADLINE = "21112018";
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_TYPE = "Loan";
    private static final String VALID_AMOUNT = "SGD 45.60";
    private static final String VALID_DEADLINE = "21/11/2018";
    private static final String VALID_NAME = BOB.getName().toString();
    private static final String VALID_PHONE = BOB.getPhone().toString();
    private static final String VALID_EMAIL = BOB.getEmail().toString();
    private static final String VALID_ADDRESS = BOB.getAddress().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = BOB.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validTransactionDetails_returnsTransaction() throws Exception {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(BOB_TRANSACTION);
        assertEquals(BOB_TRANSACTION, transaction.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_AMOUNT, VALID_TYPE, VALID_DEADLINE,
                INVALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_ADDRESS,VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_AMOUNT, VALID_TYPE, VALID_DEADLINE,
                null, VALID_EMAIL, VALID_PHONE, VALID_ADDRESS,VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_AMOUNT, VALID_TYPE, VALID_DEADLINE,
                VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_AMOUNT, VALID_TYPE, VALID_DEADLINE,
                VALID_NAME, null, VALID_PHONE, VALID_ADDRESS,VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(VALID_AMOUNT, VALID_TYPE, VALID_DEADLINE,
                VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,VALID_TAGS);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(INVALID_AMOUNT, INVALID_TYPE, INVALID_DEADLINE,
                VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(INVALID_AMOUNT, INVALID_TYPE, INVALID_DEADLINE,
                VALID_NAME, VALID_EMAIL, VALID_PHONE, INVALID_ADDRESS,VALID_TAGS);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(INVALID_AMOUNT, INVALID_TYPE, INVALID_DEADLINE,
                INVALID_NAME, VALID_EMAIL, VALID_PHONE, null,VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedTransaction transaction = new XmlAdaptedTransaction(INVALID_AMOUNT, INVALID_TYPE, INVALID_DEADLINE,
                INVALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_ADDRESS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, transaction::toModelType);
    }

}
