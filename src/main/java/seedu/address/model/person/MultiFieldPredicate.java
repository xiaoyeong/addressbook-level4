package seedu.address.model.person;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.FieldType;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class MultiFieldPredicate implements Predicate<Person> {
    private final List<FieldContainsKeywordsPredicate> predicates;

    public MultiFieldPredicate(List<FieldContainsKeywordsPredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(Person person) {
        boolean b = true;
        for(int i=0;i<predicates.size();i++){
            b = b && predicates.get(i).test(person);
        }
        return b;
    }


}
