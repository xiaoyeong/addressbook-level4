package seedu.address.model.transaction;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.FieldType;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class FieldContainsKeywordsPredicate implements Predicate<seedu.address.model.transaction.Transaction> {
    private final List<String> keywords;
    private final FieldType type;

    public FieldContainsKeywordsPredicate(FieldType type, List<String> keywords) {
        this.keywords = keywords;
        this.type = type;
    }

    @Override
    public boolean test(seedu.address.model.transaction.Transaction transaction) {
        Person person = transaction.getPerson();
        switch (type) {
        case Name:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(person.getName().fullName, keyword));
        case Email:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(person.getEmail().value, keyword));
        case Phone:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(person.getPhone().value, keyword));
        case Address:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(person.getAddress().value, keyword));
        case Deadline:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(transaction.getDeadline().value,
                            keyword));
        case Amount:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(transaction.getAmount().toString(),
                            keyword));
        case Type:
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(transaction.getType().value,
                            keyword));
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FieldContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((FieldContainsKeywordsPredicate) other).keywords))
                && type.equals(((FieldContainsKeywordsPredicate) other).type); // state check
    }

}
