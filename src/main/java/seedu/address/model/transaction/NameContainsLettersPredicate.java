package seedu.address.model.transaction;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return letters.stream().anyMatch(input -> makeRegex(input.toLowerCase(),
                transaction.getPerson().getName().fullName.trim().toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsLettersPredicate // instanceof handles nulls
                && letters.equals(((NameContainsLettersPredicate) other).letters)); // state check
    }

    /**
     * makeRegex converts String input to a regular expression (Regex).
     *
     * @param input is the substring the user keys in
     * @param name is the name to check if it contains the substring input.
     * @return true or false depending if the name contains the substring input.
     */

    public boolean makeRegex(String input, String name) {
        String regex1 = "(.*)" + input + "(.*)";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(name);
        boolean inMiddle = matcher1.matches();
        return inMiddle;
    }
}
