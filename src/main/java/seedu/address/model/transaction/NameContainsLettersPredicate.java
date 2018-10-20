package seedu.address.model.transaction;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Transaction} contains a {@code Person} matches any of the letters given.
 */
public class NameContainsLettersPredicate implements Predicate<Transaction> {
    private final List<String> letters;

    public NameContainsLettersPredicate(List<String> letters) {
        this.letters = letters;
    }

    @Override
    public boolean test(Transaction transaction) {
        return letters.stream().anyMatch(input -> check(input, transaction.getPerson().getName().fullName));
        //StringUtil.containsWordIgnoreCase(transaction.getName().fullName, input));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsLettersPredicate // instanceof handles nulls
                && letters.equals(((NameContainsLettersPredicate) other).letters)); // state check
    }

    /**
     *
     * @param input
     * @param name
     * @return
     */
    public boolean check(String input, String name) {
        char[] inputSplit = input.toCharArray();
        char[] nameSplit = name.toCharArray();

        return recursiveComparison(inputSplit, nameSplit, 0, 0);

    }

    /**
     *
     * @param input
     * @param name
     * @param i
     * @param j
     * @return
     */
    public boolean recursiveComparison(char[] input, char[] name, int i, int j) {
        while (i < input.length && j < name.length) {
            if (input[i] == (name[j])) {
                if (i == (input.length - 1)) {
                    return true;
                } else {
                    return recursiveComparison(input, name, (i + 1), j);
                }
            }
            j++;
        }
        return false;
    }

}
