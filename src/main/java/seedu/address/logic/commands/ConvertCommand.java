package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.transaction.Amount;

/**
 * Converts multiple amounts in different currencies to the base currency.
 */
public class ConvertCommand extends Command {
    public static final String COMMAND_WORD = "convert";
    public static final String COMMAND_ALIAS = "con";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Converts a given amount in foreign currency to Singapore Dollars\n"
            + "Example: " + COMMAND_WORD + " USD 20.00";

    public static final String MESSAGE_SUCCESS = "Converted Amounts: %s";

    private final List<String> amounts;

    public ConvertCommand(List<String> amounts) {
        this.amounts = amounts;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        StringBuilder convertedAmounts = new StringBuilder();
        for (int i = 0; i < amounts.size(); i += 2) {
            convertedAmounts.append(convertCurrency(amounts.get(i) + " " + amounts.get(i + 1)));
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, convertedAmounts));
    }

    /**
     * Handles the conversion of foreign currency to Singaporean currency.
     *
     * @param amount the amount in a given currency which is to be converted to Singaporean currency
     */
    private static String convertCurrency(String amount) {
        if (!Amount.isValidAmount(amount)) {
            return "";
        }
        String currencyCode = amount.split(" ")[0].toUpperCase();
        String currencyConverterFilePath = String.format(
                "http://free.currencyconverterapi.com/api/v5/convert?q=%s_SGD&compact=y", currencyCode);
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream is = new URL(currencyConverterFilePath).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            Map<String, Map<String, Double>> map = mapper.readValue(jsonText, Map.class);
            double result = map.get(String.format("%s_SGD", currencyCode)).get("val");
            result *= Double.parseDouble(amount.split(" ")[1]);
            return String.format("SGD %.2f ", result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}

